import { type GetEmployeesQueryParams, useGetEmployees, useGetEmployeeById, useGetEmployeeSummary } from "@/kubb";
import { keepPreviousData } from "@tanstack/react-query";
import { employeeQueryKeys } from "./employeeQueryKeys";

export function useEmployees(params: GetEmployeesQueryParams = {}) {
  return useGetEmployees(params, {
    query: {
      queryKey: employeeQueryKeys.list(params),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5, // 5 minutos
    },
  });
}

export function useEmployeeById(employeeId: string) {
  return useGetEmployeeById(employeeId, {
    query: {
      queryKey: employeeQueryKeys.detail(employeeId),
      enabled: !!employeeId,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useEmployeesSummary() {
  return useGetEmployeeSummary({
    query: {
      queryKey: employeeQueryKeys.summary(),
      staleTime: 1000 * 60 * 10,
    },
  });
}
