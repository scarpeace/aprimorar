import { z } from "zod"

export const createEventSchema = z.object({
  title: z.string().min(1, "Event title is required").max(100, "Title must be at most 100 characters"),
  description: z.string().max(500, "Description must be at most 500 characters").optional(),
  startDateTime: z.string().min(1, "Start date/time is required"),
  endDateTime: z.string().min(1, "End date/time is required"),
  price: z.number().min(0, "Price must be >= 0"),
  payment: z.number().min(0, "Payment must be >= 0"),
  studentId: z.string().uuid("Invalid student ID"),
  employeeId: z.string().uuid("Invalid employee ID"),
}).refine((data) => data.payment <= data.price, {
  message: "Payment can't exceed price",
  path: ["payment"],
}).refine((data) => new Date(data.endDateTime) > new Date(data.startDateTime), {
  message: "End date/time must be after start date/time",
  path: ["endDateTime"],
})

export const eventResponseSchema = z.object({
  id: z.number(),
  title: z.string(),
  description: z.string().nullable(),
  startDateTime: z.string(),
  endDateTime: z.string(),
  price: z.string(),
  payment: z.string(),
  studentId: z.string().uuid(),
  studentName: z.string(),
  employeeId: z.string().uuid(),
  employeeName: z.string(),
  createdAt: z.string(),
  updatedAt: z.string().nullable(),
})

export type CreateEventInput = z.infer<typeof createEventSchema>
export type EventResponse = z.infer<typeof eventResponseSchema>
