import { useMutation, useQuery, useQueryClient, keepPreviousData } from "@tanstack/react-query"
import { useNavigate } from "react-router-dom"
import { eventsApi } from "@/services/api"
import { queryKeys } from "@/lib/query/queryKeys"
import type { EventFormInput } from "@/lib/schemas"

// --- QUERIES ---

export function useEventsQuery(page = 0, size = 20, sortBy = "startDate", search?: string, options = {}) {
  return useQuery({
    queryKey: queryKeys.events.list({ page, size, sortBy, search }),
    queryFn: () => eventsApi.list(page, size, sortBy, search),
    placeholderData: keepPreviousData,
    ...options
  })
}

export function useEventDetailQuery(id: string) {
  return useQuery({
    queryKey: queryKeys.events.detail(id),
    queryFn: () => eventsApi.getById(id),
    enabled: !!id,
  })
}

export function useEventsByEmployeeQuery(id: string, page = 0, size = 20, options = {}) {
  return useQuery({
    queryKey: [...queryKeys.events.byEmployee(id), { page, size }],
    queryFn: () => eventsApi.listByEmployee(id, page, size),
    enabled: !!id,
    placeholderData: keepPreviousData,
    ...options
  })
}

export function useEventsByStudentQuery(id: string, page = 0, size = 20, options = {}) {
  return useQuery({
    queryKey: [...queryKeys.events.byStudent(id), { page, size }],
    queryFn: () => eventsApi.listByStudent(id, page, size),
    enabled: !!id,
    placeholderData: keepPreviousData,
    ...options
  })
}

// --- MUTATIONS ---

export function useCreateEvent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: EventFormInput) => eventsApi.create(data),
    onSuccess: (createdEvent) => {
      queryClient.invalidateQueries({ queryKey: queryKeys.events.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.students.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.employees.all })
      navigate(`/events/${createdEvent.id}`)
    },
  })
}

export function useUpdateEvent(id: string) {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: EventFormInput) => eventsApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.events.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.events.detail(id) })
      // Invalida também os alunos e colaboradores pois podem ter mudado
      queryClient.invalidateQueries({ queryKey: queryKeys.students.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.employees.all })
      navigate(`/events/${id}`)
    },
  })
}

export function useDeleteEvent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (id: string) => eventsApi.delete(id),
    onSuccess: async (_, id) => {
      navigate("/events")
      queryClient.invalidateQueries({ queryKey: queryKeys.events.lists() })
      queryClient.invalidateQueries({ queryKey: ["events", "by-student"] })
      queryClient.invalidateQueries({ queryKey: ["events", "by-employee"] })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all })
    },
  })
}
