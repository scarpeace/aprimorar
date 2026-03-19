import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { eventsApi } from "@/features/events/api/eventsApi";
import { eventsQueryKeys } from "@/features/events/query/eventsQueryKeys";

export function useEventsQuery(
  page = 0,
  size = 20,
  sortBy = "startDate",
  search?: string,
  options = {},
) {
  return useQuery({
    queryKey: eventsQueryKeys.list({ page, size, sortBy, search }),
    queryFn: () => eventsApi.list(page, size, sortBy, search),
    placeholderData: keepPreviousData,
    ...options,
  });
}

export function useEventDetailQuery(id: string) {
  return useQuery({
    queryKey: eventsQueryKeys.detail(id),
    queryFn: () => eventsApi.getById(id),
    enabled: Boolean(id),
  });
}

export function useEventsByEmployeeQuery(
  id: string,
  page = 0,
  size = 20,
  options = {},
) {
  return useQuery({
    queryKey: [...eventsQueryKeys.byEmployee(id), { page, size }],
    queryFn: () => eventsApi.listByEmployee(id, page, size),
    enabled: Boolean(id),
    placeholderData: keepPreviousData,
    ...options,
  });
}

export function useEventsByStudentQuery(
  id: string,
  page = 0,
  size = 20,
  options = {},
) {
  return useQuery({
    queryKey: [...eventsQueryKeys.byStudent(id), { page, size }],
    queryFn: () => eventsApi.listByStudent(id, page, size),
    enabled: Boolean(id),
    placeholderData: keepPreviousData,
    ...options,
  });
}
