import { useQuery } from "@tanstack/react-query";

import { parentsApi } from "@/features/parents/api/parentsApi";
import { parentsQueryKeys } from "@/features/parents/query/parentsQueryKeys";
import { studentsApi } from "@/features/students/api/studentsApi";
import { studentsQueryKeys } from "@/features/students/query/studentsQueryKeys";

export function useParentsQuery(
  page = 0,
  size = 20,
  search?: string,
  sortBy = "name",
) {
  return useQuery({
    queryKey: parentsQueryKeys.list({ page, size, sortBy, search }),
    queryFn: () => parentsApi.listPaginated(page, size, sortBy, search),
  });
}

export function useParentsListQuery() {
  return useQuery({
    queryKey: parentsQueryKeys.options,
    queryFn: () => parentsApi.list(),
  });
}

export function useParentDetailQuery(id: string) {
  return useQuery({
    queryKey: parentsQueryKeys.detail(id),
    queryFn: () => parentsApi.getById(id),
    enabled: Boolean(id),
  });
}

export function useStudentsByParentQuery(parentId: string, options = {}) {
  return useQuery({
    queryKey: studentsQueryKeys.byParent(parentId),
    queryFn: () => studentsApi.listByParent(parentId),
    enabled: Boolean(parentId),
    ...options,
  });
}
