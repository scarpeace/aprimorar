import { employeeResponseDTODutyEnum } from "@/kubb";

export const dutyLabels: Record<keyof typeof employeeResponseDTODutyEnum, string> = {
  TEACHER: "Professor",
  ADM: "Administrativo",
  THERAPIST: "Terapeuta",
  MENTOR: "Mentor",
  SYSTEM: "Sistema",
} as const;
