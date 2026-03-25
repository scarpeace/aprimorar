import {
  type GetEventsQueryParams,
  useGetEvents,
  useGetEventById,
  useGetEventsByStudent,
  useGetEventsByEmployee,
} from "@/kubb";
import { keepPreviousData } from "@tanstack/react-query";
import { eventQueryKeys } from "./eventQueryKeys";

export function useEvents(params: GetEventsQueryParams = {}) {
  return useGetEvents(params, {
    query: {
      queryKey: eventQueryKeys.list(params),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5, // 5 minutos
    },
  });
}

export function useEventById(eventId: string) {
  return useGetEventById(eventId, {
    query: {
      queryKey: eventQueryKeys.detail(eventId),
      enabled: !!eventId,
      staleTime: 1000 * 60 * 5,
    },
  });
}

export function useEventsByStudent(studentId: string) {
  return useGetEventsByStudent(
    studentId,
    {size: 10, sort: ["startDate"] },
    {
      query: {
        queryKey: eventQueryKeys.byStudent(studentId),
        staleTime: 1000 * 60 * 10,
        enabled: !!studentId
      },
    },
  );
}

export function useEventsByEmployee(employeeId: string) {
  return useGetEventsByEmployee(
    employeeId,
    {size: 10, sort: ["startDate"] },
    {
      query: {
        queryKey: eventQueryKeys.byEmployee(employeeId),
        staleTime: 1000 * 60 * 10,
        enabled: !!employeeId
      },
    },
  );
}
