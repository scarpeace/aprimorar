import type { ReactNode } from "react";
import { useMemo, useState } from "react";
import type { DateFilterContextValue } from "./date-filter-context";
import { DateFilterContext } from "./date-filter-context";

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

type DateFilterProviderProps = {
  children: ReactNode;
};

export function DateFilterProvider({ children }: Readonly<DateFilterProviderProps>) {
  const [startDate, setStartDate] = useState<Date | undefined>();
  const [endDate, setEndDate] = useState<Date | undefined>();

  const value = useMemo<DateFilterContextValue>(() => {
    const handleStartDateChange = (date: Date | null) => {
      setStartDate(date ? normalizeStartDate(date) : undefined);
    };

    const handleEndDateChange = (date: Date | null) => {
      setEndDate(date ? normalizeEndDate(date) : undefined);
    };

    const clearDateFilters = () => {
      setStartDate(undefined);
      setEndDate(undefined);
    };

    return {
      startDate,
      endDate,
      handleStartDateChange,
      handleEndDateChange,
      clearDateFilters,
      hasFilters: !!(startDate || endDate),
    };
  }, [startDate, endDate]);

  return (
    <DateFilterContext.Provider value={value}>
      {children}
    </DateFilterContext.Provider>
  );
}
