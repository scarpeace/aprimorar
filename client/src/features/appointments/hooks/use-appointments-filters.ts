import { useMemo } from "react";
import { useSearchParams } from "react-router-dom";
import {
  clearDateRangeParams,
  getDateRangeValues,
  setEndDateParam,
  setStartDateParam,
} from "@/hooks/use-date-range-filters";

export function useAppointmentsFilters() {
  const [searchParams, setSearchParams] = useSearchParams();

  const search = searchParams.get("search") ?? "";
  const { startDateStr, endDateStr } = getDateRangeValues(searchParams);
  const hideCharged = searchParams.get("hideCharged") === "true";
  const hidePaid = searchParams.get("hidePaid") === "true";
  const page = parseInt(searchParams.get("page") ?? "0", 10);

  const startDate = useMemo(() => {
    return startDateStr ? new Date(startDateStr) : null;
  }, [startDateStr]);

  const endDate = useMemo(() => {
    return endDateStr ? new Date(endDateStr) : null;
  }, [endDateStr]);

  const updateParams = (updates: Record<string, string | null>) => {
    const newParams = new URLSearchParams(searchParams);
    Object.entries(updates).forEach(([key, value]) => {
      if (value === null) {
        newParams.delete(key);
      } else {
        newParams.set(key, value);
      }
    });
    setSearchParams(newParams);
  };

  const handleSearchChange = (val: string) => {
    updateParams({ search: val || null, page: "0" });
  };

  const handleStartDateChange = (date: Date | null) => {
    const newParams = new URLSearchParams(searchParams);
    setStartDateParam(newParams, date);
    newParams.set("page", "0");
    setSearchParams(newParams);
  };

  const handleEndDateChange = (date: Date | null) => {
    const newParams = new URLSearchParams(searchParams);
    setEndDateParam(newParams, date);
    newParams.set("page", "0");
    setSearchParams(newParams);
  };

  const handleHideChargedToggle = () => {
    updateParams({ hideCharged: !hideCharged ? "true" : null, page: "0" });
  };

  const handleHidePaidToggle = () => {
    updateParams({ hidePaid: !hidePaid ? "true" : null, page: "0" });
  };

  const handlePageChange = (newPage: number) => {
    updateParams({ page: newPage > 0 ? newPage.toString() : null });
  };

  const handleClearFilters = () => {
    const newParams = new URLSearchParams(searchParams);
    clearDateRangeParams(newParams);
    newParams.delete("search");
    newParams.delete("hideCharged");
    newParams.delete("hidePaid");
    newParams.delete("page");
    setSearchParams(newParams);
  };

  return {
    search,
    startDate,
    endDate,
    hideCharged,
    hidePaid,
    page,
    handleSearchChange,
    handleStartDateChange,
    handleEndDateChange,
    handleHideChargedToggle,
    handleHidePaidToggle,
    handlePageChange,
    handleClearFilters,
    hasFilters: !!(search || startDateStr || endDateStr || hideCharged || hidePaid || page > 0),
  };
}
