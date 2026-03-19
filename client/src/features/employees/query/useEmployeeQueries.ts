import { useQuery } from "@tanstack/react-query";
import { employeesApi } from "@/features/employees/api/employeesApi";
import { employeesQueryKeys } from "@/features/employees/query/employeesQueryKeys";

export function useEmployeesQuery(
  page = 0,
  size = 20,
  search?: string,
  sortBy = "name",
) {
  return useQuery({
    queryKey: employeesQueryKeys.list({ page, size, sortBy, search }),
    queryFn: () => employeesApi.list(page, size, sortBy, search),
  });
}

export function useEmployeeDetailQuery(id: string) {
  return useQuery({
    queryKey: employeesQueryKeys.detail(id),
    queryFn: () => employeesApi.getById(id),
    enabled: Boolean(id),
  });
}

export function useEmployeeEditQuery(id: string) {
  return useQuery({
    queryKey: employeesQueryKeys.detail(id),
    queryFn: () => employeesApi.getByIdForEdit(id),
    enabled: Boolean(id),
  });
}

export function useEmployeeOptionsQuery() {
  return useQuery({
    queryKey: employeesQueryKeys.options,
    queryFn: () => employeesApi.getOptions(),
  });
}
