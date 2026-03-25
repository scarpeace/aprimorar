import type { GetEmployeesQueryParams } from "@/kubb";

export const employeeQueryKeys = {
  all: ["employees"] as const,
  lists: () => ["employees", "list"] as const,
  list: (params: GetEmployeesQueryParams = {}) =>
    ["employees", "list", params] as const,
  summary: () => ["employees", "list", "summary"] as const,
  details: () => ["employees", "detail"] as const,
  detail: (employeeId: string) => ["employees", "detail", employeeId] as const,
} as const;

export type EmployeesListQueryKey = ReturnType<typeof employeeQueryKeys.list>;
export type EmployeeDetailQueryKey = ReturnType<typeof employeeQueryKeys.detail
>;
