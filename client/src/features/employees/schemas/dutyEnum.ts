import type { Duty } from "@/lib/shared/enums"

export const dutyLabels = {
  TEACHER: "Professor(a)",
  ADM: "Administrativo",
  THERAPIST: "Terapeuta",
  MENTOR: "Mentor(a)",
  SYSTEM: "Sistema",
} as const satisfies Record<Duty, string>
