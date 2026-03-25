import {
  useGetParentById,
  useGetParents,
  useGetParentsSummary,
  type GetParentsQueryParams,
} from "@/kubb";
import { keepPreviousData } from "@tanstack/react-query";
import { parentQueryKeys } from "./parentQueryKeys";

export function useParents(params: GetParentsQueryParams = {}) {
  return useGetParents(params, {
    query: {
      queryKey: parentQueryKeys.list(params),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5, // 5 minutos
    },
  });
}

export function useParentById(parentId: string) {
  return useGetParentById(parentId, {
    query: {
      queryKey: parentQueryKeys.detail(parentId),
      enabled: !!parentId,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useParentsSummary() {
  return useGetParentsSummary({
    query: {
      queryKey: parentQueryKeys.summary(),
      staleTime: 1000 * 60 * 10,
    },
  });
}
