import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { dashboardQueryKeys } from "@/features/dashboard/query/dashboardQueryKeys";
import { eventsApi } from "@/features/events/api/eventsApi";
import { eventsQueryKeys } from "@/features/events/query/eventsQueryKeys";
import { studentsQueryKeys } from "@/features/students/query/studentsQueryKeys";
import { employeesQueryKeys } from "@/features/employees/query/employeesQueryKeys";
import type { EventFormInput } from "@/lib/schemas";

export function useCreateEvent() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: EventFormInput) => eventsApi.create(data),
    onSuccess: (createdEvent) => {
      toast.success("Evento criado com sucesso!");

      queryClient.invalidateQueries({ queryKey: eventsQueryKeys.all });
      queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all });
      queryClient.invalidateQueries({ queryKey: studentsQueryKeys.all });
      queryClient.invalidateQueries({ queryKey: employeesQueryKeys.all });

      navigate(`/events/${createdEvent.id}`);
    },
  });
}

export function useUpdateEvent(id: string) {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: EventFormInput) => eventsApi.update(id, data),
    onSuccess: () => {
      toast.success("Evento atualizado com sucesso!");

      queryClient.invalidateQueries({ queryKey: eventsQueryKeys.all });
      queryClient.invalidateQueries({ queryKey: eventsQueryKeys.detail(id) });
      queryClient.invalidateQueries({ queryKey: studentsQueryKeys.all });
      queryClient.invalidateQueries({ queryKey: employeesQueryKeys.all });

      navigate(`/events/${id}`);
    },
  });
}

export function useDeleteEvent() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (id: string) => eventsApi.delete(id),
    onSuccess: async () => {
      toast.success("Evento deletado com sucesso!");
      navigate("/events");

      queryClient.invalidateQueries({ queryKey: eventsQueryKeys.lists() });
      queryClient.invalidateQueries({
        queryKey: eventsQueryKeys.byStudentPrefix(),
      });
      queryClient.invalidateQueries({
        queryKey: eventsQueryKeys.byEmployeePrefix(),
      });
      queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all });
    },
  });
}
