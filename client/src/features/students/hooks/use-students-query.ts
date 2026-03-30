import {
    getStudentByIdQueryKey,
    getStudentsByParentQueryKey,
    getStudentsQueryKey,
    useGetStudentById,
    useGetStudents,
    useGetStudentsByParent,
    type GetStudentByIdPathParams,
    type GetStudentsByParentPathParams,
} from "@/kubb";
import { keepPreviousData } from "@tanstack/react-query";

import { studentResponseSchema } from "./studentSchema";

export function useStudentsQuery(params: GetStudentsQueryParams) {
  return useGetStudents(params, {
    query: {
      queryKey: getStudentsQueryKey(params),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useStudentByIdQuery(params: GetStudentByIdPathParams) {
  return useGetStudentById(params.studentId, {
    query: {
      select: (data) => studentResponseSchema.parse(data),
      queryKey: getStudentByIdQueryKey(params.studentId),
      enabled: !!params.studentId,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useStudentsByParentQuery(params: GetStudentsByParentPathParams) {
  return useGetStudentsByParent(params.parentId, {
    query: {
      queryKey: getStudentsByParentQueryKey(params.parentId),
      enabled: !!params.parentId,
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useStudentsSummary() {
  return useStudentsSummary();
}
