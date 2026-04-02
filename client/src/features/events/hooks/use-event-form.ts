import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import type { EventResponseDTO, StudentResponseDTO } from "@/kubb";
import { eventCreateSchema, type EventCreateSchema } from "./eventSchema";

export function useStudentForm(event?: EventResponseDTO) {

  const form = useForm<EventCreateSchema>({
    resolver: zodResolver(eventCreateSchema),
    mode: "onBlur",
    values: {
      title: event?.title ?? "",
      description: event?.description ?? "",
      startDate: event?.startDate ?? null,
      endDate: event?.endDate ?? "",
      price: event?.price ?? 0,
      payment: event?.payment ?? 0,
      content: event?.content ?? "",
      studentName: event?.studentName ?? "",
      employeeName: event?.employeeId ?? "",
    },
  });

  const registerWithMask = useHookFormMask(form.register);

  return {
    ...form,
    errors: form.formState.errors,
    registerWithMask,
  };
}
