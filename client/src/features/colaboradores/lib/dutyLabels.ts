import { colaboradorResponseDTOFuncaoEnum } from "@/kubb";

export const dutyLabels: Record<keyof typeof colaboradorResponseDTOFuncaoEnum, string> = {
  PROFESSOR: "Professor",
  ADMINISTRATIVO: "Administrativo",
  TERAPEUTA: "Terapeuta",
  MENTOR: "Mentor",
  SISTEMA: "Sistema",
} as const;
