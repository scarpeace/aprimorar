import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { useNavigate } from "react-router-dom"
import { eventsApi } from "@/services/api"
import { queryKeys } from "@/lib/query/queryKeys"
import type { EventFormInput } from "@/lib/schemas"

// --- QUERIES ---

export function useEventsQuery(page = 0, size = 20, sortBy = "startDate") {
  return useQuery({
    queryKey: [...queryKeys.events, "list", { page, size, sortBy }],
    queryFn: () => eventsApi.list(page, size, sortBy),
  })
}

export function useEventDetailQuery(id: string) {
  return useQuery({
    queryKey: [...queryKeys.events, id],
    queryFn: () => eventsApi.getById(id),
    enabled: !!id,
  })
}

// --- MUTATIONS ---

export function useCreateEvent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: EventFormInput) => eventsApi.create(data),
    onSuccess: (createdEvent) => {
      queryClient.invalidateQueries({ queryKey: queryKeys.events })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard })
      queryClient.invalidateQueries({ queryKey: queryKeys.students })
      queryClient.invalidateQueries({ queryKey: queryKeys.employees })
      navigate(`/events/${createdEvent.id}`)
    },
  })
}

export function useUpdateEvent(id: string) {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: EventFormInput) => eventsApi.update(id, data),
    onSuccess: (updatedEvent) => {
      queryClient.invalidateQueries({ queryKey: queryKeys.events })
      queryClient.setQueryData([...queryKeys.events, id], updatedEvent)
      // Invalida também os alunos e colaboradores pois podem ter mudado
      queryClient.invalidateQueries({ queryKey: queryKeys.students })
      queryClient.invalidateQueries({ queryKey: queryKeys.employees })
      navigate(`/events/${id}`)
    },
  })
}

export function useDeleteEvent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (id: string) => eventsApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.events })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard })
      queryClient.invalidateQueries({ queryKey: queryKeys.students })
      queryClient.invalidateQueries({ queryKey: queryKeys.employees })
      navigate("/events")
    },
  })
}

