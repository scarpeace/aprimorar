import { useCallback, useState } from "react";

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

export function usePageDateFilter(): PageDateFilter {
  const [startDate, setStartDate] = useState<Date | undefined>();
  const [endDate, setEndDate] = useState<Date | undefined>();

  const handleStartDateChange = useCallback((date: Date | null) => {
    setStartDate(date ? normalizeStartDate(date) : undefined);
  }, []);

  const handleEndDateChange = useCallback((date: Date | null) => {
    setEndDate(date ? normalizeEndDate(date) : undefined);
  }, []);

  const clearDateFilters = useCallback(() => {
    setStartDate(undefined);
    setEndDate(undefined);
  }, []);

  return {
    startDate,
    endDate,
    handleStartDateChange,
    handleEndDateChange,
    clearDateFilters,
    hasFilters: !!(startDate || endDate),
  };
}
