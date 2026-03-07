import type { CreateEventInput, EventResponse } from "@/lib/schemas"

export function toDateTimeLocalValue(value: string | undefined) {
  if (!value) return ""

  const normalized = value.replace("Z", "")
  return normalized.slice(0, 16)
}

export function mapEventResponseToFormValues(event: EventResponse): CreateEventInput {
  return {
    title: event.title,
    description: event.description ?? "",
    startDateTime: toDateTimeLocalValue(event.startDateTime),
    endDateTime: toDateTimeLocalValue(event.endDateTime),
    price: Number(event.price),
    payment: Number(event.payment),
    content: event.content,
    studentId: event.studentId,
    employeeId: event.employeeId,
  }
}
