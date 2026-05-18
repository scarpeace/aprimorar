import { useCallback, useMemo, useState } from "react";

type StoredDateFilter = {
  version: 1;
  startDate?: string;
  endDate?: string;
};

export type PageDateFilter = {
  startDate?: Date;
  endDate?: Date;
  handleStartDateChange: (date: Date | null) => void;
  handleEndDateChange: (date: Date | null) => void;
  clearDateFilters: () => void;
  hasFilters: boolean;
};

function normalizeStartDate(date: Date) {
  const normalizedDate = new Date(date);
  normalizedDate.setHours(0, 0, 0, 0);
  return normalizedDate;
}

function normalizeEndDate(date: Date) {
  const normalizedDate = new Date(date);
  normalizedDate.setHours(23, 59, 59, 999);
  return normalizedDate;
}

function getStorageKey(pageKey: string) {
  return `aprimorar:date-filter:${pageKey}:v1`;
}

function parseStoredDate(value?: string) {
  if (!value) {
    return undefined;
  }

  const date = new Date(value);
  return Number.isNaN(date.getTime()) ? undefined : date;
}

function readStoredFilter(pageKey: string) {
  try {
    const rawValue = window.localStorage.getItem(getStorageKey(pageKey));
    if (!rawValue) {
      return {};
    }

    const parsedValue = JSON.parse(rawValue) as StoredDateFilter;
    if (parsedValue.version !== 1) {
      return {};
    }

    return {
      startDate: parseStoredDate(parsedValue.startDate),
      endDate: parseStoredDate(parsedValue.endDate),
    };
  } catch {
    return {};
  }
}

function writeStoredFilter(pageKey: string, startDate?: Date, endDate?: Date) {
  try {
    if (!startDate && !endDate) {
      window.localStorage.removeItem(getStorageKey(pageKey));
      return;
    }

    const value: StoredDateFilter = {
      version: 1,
      startDate: startDate?.toISOString(),
      endDate: endDate?.toISOString(),
    };

    window.localStorage.setItem(getStorageKey(pageKey), JSON.stringify(value));
  } catch {
    // Ignore storage errors so the filter still works in memory.
  }
}

export function usePageDateFilter(pageKey: string): PageDateFilter {
  const initialFilter = useMemo(() => readStoredFilter(pageKey), [pageKey]);
  const [startDate, setStartDate] = useState<Date | undefined>(initialFilter.startDate);
  const [endDate, setEndDate] = useState<Date | undefined>(initialFilter.endDate);

  const handleStartDateChange = useCallback(
    (date: Date | null) => {
      const nextStartDate = date ? normalizeStartDate(date) : undefined;
      setStartDate(nextStartDate);
      writeStoredFilter(pageKey, nextStartDate, endDate);
    },
    [endDate, pageKey],
  );

  const handleEndDateChange = useCallback(
    (date: Date | null) => {
      const nextEndDate = date ? normalizeEndDate(date) : undefined;
      setEndDate(nextEndDate);
      writeStoredFilter(pageKey, startDate, nextEndDate);
    },
    [pageKey, startDate],
  );

  const clearDateFilters = useCallback(() => {
    setStartDate(undefined);
    setEndDate(undefined);
    writeStoredFilter(pageKey);
  }, [pageKey]);

  return {
    startDate,
    endDate,
    handleStartDateChange,
    handleEndDateChange,
    clearDateFilters,
    hasFilters: !!(startDate || endDate),
  };
}
