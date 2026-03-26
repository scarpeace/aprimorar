import type { GetStudentsQueryParams } from "@/kubb/types/student/GetStudents";
import { keepPreviousData } from "@tanstack/react-query";
import {
  useGetStudentById,
  useGetStudents,
  useGetStudentsByParent,
  useGetStudentSummary,
} from "@/kubb";

import { studentsQueryKeys } from "./studentQueryKeys";

export function useStudentsQuery(params: GetStudentsQueryParams = {}) {
  return useGetStudents(params, {
    query: {
      queryKey: studentsQueryKeys.list(params),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useStudentByIdQuery(studentId: string) {
  return useGetStudentById(studentId, {
    query: {
      queryKey: studentsQueryKeys.detail(studentId),
      enabled: !!studentId,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useStudentsByParentQuery(parentId: string) {
  return useGetStudentsByParent(parentId, {
    query: {
      queryKey: studentsQueryKeys.byParent(parentId),
      enabled: !!parentId,
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useStudentsSummary() {
  return useGetStudentSummary({
    query: {
      queryKey: studentsQueryKeys.summary(),
      staleTime: 1000 * 60 * 10,
    },
  });
}
