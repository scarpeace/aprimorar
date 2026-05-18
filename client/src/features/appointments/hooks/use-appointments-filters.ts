import { useSearchParams } from "react-router-dom";
import { useDateFilter } from "@/hooks/use-date-filter";

export function useAppointmentsFilters() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { startDate, endDate, clearDateFilters } = useDateFilter();

  const search = searchParams.get("search") ?? "";
  const hideCharged = searchParams.get("hideCharged") === "true";
  const hidePaid = searchParams.get("hidePaid") === "true";
  const page = parseInt(searchParams.get("page") ?? "0", 10);

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
    newParams.delete("search");
    newParams.delete("hideCharged");
    newParams.delete("hidePaid");
    newParams.delete("page");
    setSearchParams(newParams);
    clearDateFilters();
  };

  return {
    search,
    startDate,
    endDate,
    hideCharged,
    hidePaid,
    page,
    handleSearchChange,
    handleHideChargedToggle,
    handleHidePaidToggle,
    handlePageChange,
    handleClearFilters,
    hasFilters: !!(search || startDate || endDate || hideCharged || hidePaid || page > 0),
  };
}
