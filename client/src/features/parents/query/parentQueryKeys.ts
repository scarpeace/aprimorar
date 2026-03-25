import type { GetParentsQueryParams } from "@/kubb";

export const parentQueryKeys = {
  all: ["parents"] as const,
  lists: () => ["parents", "list"] as const,
  list: (params: GetParentsQueryParams = {}) =>
    ["parents", "list", params] as const,
    details: () => ["parents", "detail"] as const,
    detail: (parentId: string) => ["parents", "detail", parentId] as const,
  byParent: (parentId: string) =>
    ["parents", "by-parent", parentId] as const,
    summary: () => ["parents", "options"] as const,
} as const;

export type ParentsListQueryKey = ReturnType<typeof parentQueryKeys.list>;
export type ParentDetailQueryKey = ReturnType<typeof parentQueryKeys.detail>;
export type ParentsByParentQueryKey = ReturnType<typeof parentQueryKeys.byParent>;
