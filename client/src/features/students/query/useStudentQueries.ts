import { useQuery } from "@tanstack/react-query";
import { studentsApi } from "@/features/students/api/studentsApi";
import { studentsQueryKeys } from "@/features/students/query/studentsQueryKeys";

export function useStudentsQuery(
  page: number,
  size: number,
  search?: string,
  options = {},
) {
  return useQuery({
    queryKey: studentsQueryKeys.list({ page, size, search }),
    queryFn: () => studentsApi.list(page, size, "name", search),
    staleTime: 1000 * 60 * 5,
    ...options,
  });
}

export function useStudentDetailQuery(id: string) {
  return useQuery({
    queryKey: studentsQueryKeys.detail(id),
    queryFn: () => studentsApi.getById(id),
    enabled: Boolean(id),
  });
}

export function useStudentOptionsQuery() {
  return useQuery({
    queryKey: studentsQueryKeys.options,
    queryFn: () => studentsApi.getOptions(),
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
