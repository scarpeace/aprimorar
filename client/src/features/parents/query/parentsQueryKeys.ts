export const parentsQueryKeys = {
  all: ["parents"] as const,
  lists: () => ["parents", "list"] as const,
  list: (params: Record<string, unknown>) => ["parents", "list", params] as const,
  details: () => ["parents", "detail"] as const,
  detail: (id: string) => ["parents", "detail", id] as const,
  options: ["parents", "options"] as const,
} as const;
