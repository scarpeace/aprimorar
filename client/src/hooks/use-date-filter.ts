import { useContext } from "react";
import { DateFilterContext } from "@/contexts/date-filter-context";

export function useDateFilter() {
  const context = useContext(DateFilterContext);

  if (!context) {
    throw new Error("useDateFilter must be used within DateFilterProvider");
  }

  return context;
}
