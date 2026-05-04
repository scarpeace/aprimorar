import { useMemo } from "react";
import { useSearchParams } from "react-router-dom";

export function useEventsFilters() {
  const [searchParams, setSearchParams] = useSearchParams();

  const search = searchParams.get("search") ?? "";
  const startDateStr = searchParams.get("startDate");
  const endDateStr = searchParams.get("endDate");
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
    if (!date) {
      updateParams({ startDate: null, page: "0" });
      return;
    }
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    updateParams({ startDate: d.toISOString(), page: "0" });
  };

  const handleEndDateChange = (date: Date | null) => {
    if (!date) {
      updateParams({ endDate: null, page: "0" });
      return;
    }
    const d = new Date(date);
    d.setHours(23, 59, 59, 999);
    updateParams({ endDate: d.toISOString(), page: "0" });
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
    setSearchParams(new URLSearchParams());
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
