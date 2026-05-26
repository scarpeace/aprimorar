import { colaboradorResponseDTODutyEnum } from "@/kubb";

export const dutyLabels: Record<keyof typeof colaboradorResponseDTODutyEnum, string> = {
  TEACHER: "Professor",
  ADM: "Administrativo",
  THERAPIST: "Terapeuta",
  MENTOR: "Mentor",
  SYSTEM: "Sistema",
} as const;
