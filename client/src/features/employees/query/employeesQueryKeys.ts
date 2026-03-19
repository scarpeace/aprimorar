export const employeesQueryKeys = {
  all: ["employees"] as const,
  lists: () => ["employees", "list"] as const,
  list: (params: Record<string, unknown>) => ["employees", "list", params] as const,
  details: () => ["employees", "detail"] as const,
  detail: (id: string) => ["employees", "detail", id] as const,
  options: ["employees", "options"] as const,
} as const;
