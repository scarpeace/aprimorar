import type { GetStudentsQueryParams } from "@/kubb";

export const studentsQueryKeys = {
  all: ["students"] as const,
  lists: () => ["students", "list"] as const,
  list: (params: GetStudentsQueryParams = {}) =>
    ["students", "list", params] as const,
  details: () => ["students", "detail"] as const,
  detail: (studentId: string) => ["students", "detail", studentId] as const,
  byParent: (parentId: string) =>
    ["students", "by-parent", parentId] as const,
  summary: () => ["students", "summary"] as const,
} as const;

export type StudentsListQueryKey = ReturnType<typeof studentsQueryKeys.list>;
export type StudentDetailQueryKey = ReturnType<typeof studentsQueryKeys.detail>;
export type StudentsByParentQueryKey = ReturnType<typeof studentsQueryKeys.byParent>;
