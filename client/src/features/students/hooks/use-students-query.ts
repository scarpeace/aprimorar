import {
    getStudentByIdQueryKey,
    getStudentsOptionsQueryKey,
    getStudentsQueryKey,
    useGetStudentById,
    useGetStudents,
    useGetStudentsOptions,
    type GetStudentByIdPathParams,
    type GetStudentsQueryParams
} from "@/kubb";
import { keepPreviousData } from "@tanstack/react-query";

export function useStudents(params: GetStudentsQueryParams) {
  return useGetStudents(params, {
    query: {
      queryKey: getStudentsQueryKey(params),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useStudentById(params: GetStudentByIdPathParams) {
  return useGetStudentById(params.studentId, {
    query: {
      queryKey: getStudentByIdQueryKey(params.studentId),
      placeholderData: keepPreviousData,
      enabled: !!params.studentId,
      staleTime: 1000 * 60 * 5,
    },
  });
}

// export function useStudentsByParent(
//   params: GetStudentsByParentPathParams,
// ) {
//   return useGetStudentsByParent(params.parentId, {
//     query: {
//       queryKey: getStudentsByParentQueryKey(params.parentId),
//       enabled: !!params.parentId,
//       placeholderData: keepPreviousData,
//       staleTime: 1000 * 60 * 5,
//     },
//   });
// }

export function useStudentsOption() {
  return useGetStudentsOptions({
    query: {
      queryKey: getStudentsOptionsQueryKey(),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5,
    },
  });
}
