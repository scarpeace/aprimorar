import { useMemo } from "react";
import { useSearchParams } from "react-router-dom";

export function useEmployeeDateFilters() {
  const [searchParams, setSearchParams] = useSearchParams();

  const startDateStr = searchParams.get("startDate");
  const endDateStr = searchParams.get("endDate");

  const startDate = useMemo(() => {
    return startDateStr ? new Date(startDateStr) : undefined;
  }, [startDateStr]);

  const endDate = useMemo(() => {
    return endDateStr ? new Date(endDateStr) : undefined;
  }, [endDateStr]);

  const handleStartDateChange = (date: Date | null) => {
    if (date === null) return;
    const newParams = new URLSearchParams(searchParams);
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    newParams.set("startDate", d.toISOString());
    setSearchParams(newParams);
  };

  const handleEndDateChange = (date: Date | null) => {
    if (date === null) return;
    const newParams = new URLSearchParams(searchParams);
    const d = new Date(date);
    d.setHours(23, 59, 59, 999);
    newParams.set("endDate", d.toISOString());
    setSearchParams(newParams);
  };

  const handleClearFilters = () => {
    setSearchParams(new URLSearchParams());
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
