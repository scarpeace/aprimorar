import { colaboradorResponseDTOFuncaoEnum } from "@/kubb";

export const colaboradorConstants: Record<keyof typeof colaboradorResponseDTOFuncaoEnum, string> = {
  PROFESSOR: "Professor",
  ADMINISTRATIVO: "Administrativo",
  TERAPEUTA: "Terapeuta",
  MENTOR: "Mentor",
  SISTEMA: "Sistema",
} as const;
