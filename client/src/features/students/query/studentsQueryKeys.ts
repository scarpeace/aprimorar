export const studentsQueryKeys = {
  all: ["students"] as const,
  lists: () => ["students", "list"] as const,
  list: (params: Record<string, unknown>) =>
    ["students", "list", params] as const,
  details: () => ["students", "detail"] as const,
  detail: (id: string) => ["students", "detail", id] as const,
  byParentPrefix: () => ["students", "by-parent"] as const,
  byParent: (parentId: string) => ["students", "by-parent", parentId] as const,
  options: ["students", "options"] as const,
} as const;
