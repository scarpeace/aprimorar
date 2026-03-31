import {
  getStudentByIdQueryKey,
  getStudentsByParentQueryKey,
  getStudentsOptionsQueryKey,
  getStudentsQueryKey,
  useGetStudentById,
  useGetStudents,
  useGetStudentsByParent,
  useGetStudentsOptions,
  type GetStudentByIdPathParams,
  type GetStudentsByParentPathParams,
  type GetStudentsQueryParams,
} from "@/kubb";
import { keepPreviousData } from "@tanstack/react-query";

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
      queryKey: getStudentByIdQueryKey(params.studentId),
      placeholderData: keepPreviousData,
      enabled: !!params.studentId,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useStudentsByParentQuery(
  params: GetStudentsByParentPathParams,
) {
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
  return useGetStudentsOptions({
    query: {
      queryKey: getStudentsOptionsQueryKey(),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5,
    },
  });
}
