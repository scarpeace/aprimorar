import {
  type GetEventsQueryParams,
  useGetEvents,
  useGetEventById,
  useGetEventsByStudent,
  useGetEventsByEmployee,
  getEventsQueryKey,
  getEventByIdQueryKey,
  getEventsByStudentQueryKey,
  getEventsByEmployeeQueryKey,
} from "@/kubb";
import { keepPreviousData } from "@tanstack/react-query";

export function useEvents(params: GetEventsQueryParams = {}) {
  return useGetEvents(params, {
    query: {
      queryKey: getEventsQueryKey(params),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5, // 5 minutos
    },
  });
}

export function useEventById(eventId: string) {
  return useGetEventById(eventId, {
    query: {
      queryKey: getEventByIdQueryKey(eventId),
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
        queryKey: getEventsByStudentQueryKey(studentId),
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
        queryKey: getEventsByEmployeeQueryKey(employeeId),
        staleTime: 1000 * 60 * 10,
        enabled: !!employeeId
      },
    },
  );
}
