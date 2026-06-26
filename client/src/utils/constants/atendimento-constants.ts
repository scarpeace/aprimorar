import {
  atendimentoResponseStatusEnum,
  atendimentoResponseTipoEnum,
  type CalendarioAtendimentosRespose,
} from "@/kubb";

export const tipoAtendimentoLabels: Record<string, string> = {
  AULA: "Aula",
  MENTORIA: "Mentoria",
  TERAPIA: "Terapia",
  ORIENTACAO_VOCACIONAL: "Orientação Vocacional",
  ENEM: "Enem",
  PAS: "PAS",
  OUTRO: "Outro",
};

export const statusOptions = [
  { value: atendimentoResponseStatusEnum.AGENDADO, label: "Agendados" },
  { value: atendimentoResponseStatusEnum.CONCLUIDO, label: "Concluídos" },
  { value: atendimentoResponseStatusEnum.CANCELADO, label: "Cancelados" },
] as const;

export const tipoOptions = [
  { value: atendimentoResponseTipoEnum.AULA, label: tipoAtendimentoLabels.AULA },
  { value: atendimentoResponseTipoEnum.MENTORIA, label: tipoAtendimentoLabels.MENTORIA },
  { value: atendimentoResponseTipoEnum.TERAPIA, label: tipoAtendimentoLabels.TERAPIA },
  { value: atendimentoResponseTipoEnum.ORIENTACAO_VOCACIONAL, label: tipoAtendimentoLabels.ORIENTACAO_VOCACIONAL },
  { value: atendimentoResponseTipoEnum.ENEM, label: tipoAtendimentoLabels.ENEM },
  { value: atendimentoResponseTipoEnum.PAS, label: tipoAtendimentoLabels.PAS },
  { value: atendimentoResponseTipoEnum.OUTRO, label: tipoAtendimentoLabels.OUTRO },
] as const;

export const monthTabs = [
  "Janeiro",
  "Fevereiro",
  "Março",
  "Abril",
  "Maio",
  "Junho",
  "Julho",
  "Agosto",
  "Setembro",
  "Outubro",
  "Novembro",
  "Dezembro",
] as const;

export const CORES_TIPO_ATENDIMENTO: Record<string, { backgroundColor: string; borderColor: string }> = {
  AULA: { backgroundColor: "#3b82f6", borderColor: "#2563eb" },
  MENTORIA: { backgroundColor: "#10b981", borderColor: "#059669" },
  TERAPIA: { backgroundColor: "#f59e0b", borderColor: "#d97706" },
  ORIENTACAO_VOCACIONAL: { backgroundColor: "#8b5cf6", borderColor: "#7c3aed" },
  ENEM: { backgroundColor: "#ec4899", borderColor: "#db2777" },
  PAS: { backgroundColor: "#06b6d4", borderColor: "#0891b2" },
  OUTRO: { backgroundColor: "#6b7280", borderColor: "#4b5563" },
};

export function getCorAtendimento(atendimento: CalendarioAtendimentosRespose) {
  return CORES_TIPO_ATENDIMENTO[atendimento.tipo ?? "OUTRO"] ?? CORES_TIPO_ATENDIMENTO.OUTRO;
}
