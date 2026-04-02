import { type GetParentsQueryParams, useGetParents, getParentsQueryKey, type GetParentByIdPathParams, useGetParentById, getParentByIdQueryKey, useGetParentsOptions, getParentsOptionsQueryKey } from "@/kubb";
import { keepPreviousData } from "@tanstack/react-query";

export function useParents(params: GetParentsQueryParams) {
  return useGetParents(params, {
    query: {
      queryKey: getParentsQueryKey(params),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useParentById(params: GetParentByIdPathParams) {
  return useGetParentById(params.parentId, {
    query: {
      queryKey: getParentByIdQueryKey(params.parentId),
      placeholderData: keepPreviousData,
      enabled: !!params.parentId,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useParentsSummary() {
  return useGetParentsOptions({
    query: {
      queryKey: getParentsOptionsQueryKey(),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5,
    },
  });
}
