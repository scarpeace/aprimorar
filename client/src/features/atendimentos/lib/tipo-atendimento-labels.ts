import type { AtendimentoResponseDTO } from "@/kubb";

export const tipoAtendimentoLabels: Record<AtendimentoResponseDTO["tipo"], string> = {
  AULA: "Aula",
  MENTORIA: "Mentoria",
  TERAPIA: "Terapia",
  ORIENTACAO_VOCACIONAL: "Orientação Vocacional",
  ENEM: "Enem",
  PAS: "PAS",
  OUTRO: "Outro",
};
