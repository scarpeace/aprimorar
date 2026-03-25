import type { GetEventsQueryParams } from "@/kubb";

export const eventQueryKeys = {
  all: ["events"] as const,
  lists: () => ["events", "list"] as const, //TODO: Eu acho que pode remover essa chave e usar só a debaixo em todas as keyFactories
  list: (params: GetEventsQueryParams = {}) =>
    ["events", "list", params] as const,
  details: () => ["events", "detail"] as const,//TODO: essa também
  detail: (eventId: string) => ["events", "detail", eventId] as const,
  byStudent: (studentId: string) => ["events", "by-event", studentId] as const,
  byEmployee: (employeeId: string) => ["events", "by-event", employeeId] as const,
} as const;

export type EventsListQueryKey = ReturnType<typeof eventQueryKeys.list>;
export type EventDetailQueryKey = ReturnType<typeof eventQueryKeys.detail>;
export type EventsByStudentQueryKey = ReturnType<typeof eventQueryKeys.byStudent>;
export type EventsByEmployeeQueryKey = ReturnType<typeof eventQueryKeys.byEmployee>;
