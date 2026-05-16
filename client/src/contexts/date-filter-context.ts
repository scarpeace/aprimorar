import { createContext } from "react";

export type DateFilterContextValue = {
  startDate?: Date;
  endDate?: Date;
  handleStartDateChange: (date: Date | null) => void;
  handleEndDateChange: (date: Date | null) => void;
  clearDateFilters: () => void;
  hasFilters: boolean;
};

export const DateFilterContext = createContext<DateFilterContextValue | null>(null);
