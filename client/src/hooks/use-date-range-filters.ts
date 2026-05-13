import { useMemo } from "react";
import { useSearchParams } from "react-router-dom";

export function getDateRangeValues(searchParams: URLSearchParams) {
  const startDateStr = searchParams.get("startDate");
  const endDateStr = searchParams.get("endDate");

  return {
    startDateStr,
    endDateStr,
    startDate: startDateStr ? new Date(startDateStr) : undefined,
    endDate: endDateStr ? new Date(endDateStr) : undefined,
  };
}

export function setStartDateParam(searchParams: URLSearchParams, date: Date | null) {
  if (!date) {
    searchParams.delete("startDate");
    return;
  }

  const normalizedDate = new Date(date);
  normalizedDate.setHours(0, 0, 0, 0);
  searchParams.set("startDate", normalizedDate.toISOString());
}

export function setEndDateParam(searchParams: URLSearchParams, date: Date | null) {
  if (!date) {
    searchParams.delete("endDate");
    return;
  }

  const normalizedDate = new Date(date);
  normalizedDate.setHours(23, 59, 59, 999);
  searchParams.set("endDate", normalizedDate.toISOString());
}

export function clearDateRangeParams(searchParams: URLSearchParams) {
  searchParams.delete("startDate");
  searchParams.delete("endDate");
}

export function useDateRangeFilters() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { startDateStr, endDateStr } = getDateRangeValues(searchParams);

  const startDate = useMemo(() => {
    return startDateStr ? new Date(startDateStr) : undefined;
  }, [startDateStr]);

  const endDate = useMemo(() => {
    return endDateStr ? new Date(endDateStr) : undefined;
  }, [endDateStr]);

  const handleStartDateChange = (date: Date | null) => {
    const newParams = new URLSearchParams(searchParams);
    setStartDateParam(newParams, date);
    setSearchParams(newParams);
  };

  const handleEndDateChange = (date: Date | null) => {
    const newParams = new URLSearchParams(searchParams);
    setEndDateParam(newParams, date);
    setSearchParams(newParams);
  };

  const handleClearFilters = () => {
    const newParams = new URLSearchParams(searchParams);
    clearDateRangeParams(newParams);
    setSearchParams(newParams);
  };

  return {
    startDate,
    endDate,
    handleStartDateChange,
    handleEndDateChange,
    handleClearFilters,
    hasFilters: !!(startDate || endDate),
  };
}
