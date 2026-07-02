import { atendimentoResponseStatusEnum, atendimentoResponseTipoEnum } from "@/lib/api/generated/types/AtendimentoResponse";
import type { CalendarioAtendimentosResponseTipoEnumKey } from "@/lib/api/generated/types/CalendarioAtendimentosResponse";

export const tipoAtendimentoLabels: Record<string, string> = {
  AULA: "Aula",
  MENTORIA: "Mentoria",
  TERAPIA: "Terapia",
  ORIENTACAO_VOCACIONAL: "Orientação Vocacional",
  ENEM: "Enem",
  PAS: "PAS",
  OUTRO: "Outro",
};

export const statusAtendimentoLabels: Record<string, string> = {
  AGENDADO: "Agendado",
  CONCLUIDO: "Concluído",
  CANCELADO: "Cancelado",
};

export const atendimentoStatusOptions = [
  { value: "", label: "Todos os status" },
  { value: atendimentoResponseStatusEnum.AGENDADO, label: "Agendado" },
  { value: atendimentoResponseStatusEnum.CONCLUIDO, label: "Concluído" },
  { value: atendimentoResponseStatusEnum.CANCELADO, label: "Cancelado" },
] as const;

export const atendimentoTipoOptions = [
  { value: "", label: "Todos os tipos" },
  { value: atendimentoResponseTipoEnum.AULA, label: tipoAtendimentoLabels.AULA },
  { value: atendimentoResponseTipoEnum.MENTORIA, label: tipoAtendimentoLabels.MENTORIA },
  { value: atendimentoResponseTipoEnum.TERAPIA, label: tipoAtendimentoLabels.TERAPIA },
  { value: atendimentoResponseTipoEnum.ORIENTACAO_VOCACIONAL, label: tipoAtendimentoLabels.ORIENTACAO_VOCACIONAL },
  { value: atendimentoResponseTipoEnum.ENEM, label: tipoAtendimentoLabels.ENEM },
  { value: atendimentoResponseTipoEnum.PAS, label: tipoAtendimentoLabels.PAS },
  { value: atendimentoResponseTipoEnum.OUTRO, label: tipoAtendimentoLabels.OUTRO },
] as const;

export const atendimentoMonthTabs = [
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

export const atendimentoTipoBadgeClass: Record<string, string> = {
  AULA: "badge-primary",
  MENTORIA: "badge-info",
  TERAPIA: "badge-success",
  ORIENTACAO_VOCACIONAL: "badge-secondary",
  ENEM: "badge-error",
  PAS: "badge-accent",
  OUTRO: "badge-ghost",
};

export const atendimentoTipoCalendarColor: Record<string, { backgroundColor: string; borderColor: string }> = {
  AULA: { backgroundColor: "#3b82f6", borderColor: "#2563eb" },
  MENTORIA: { backgroundColor: "#10b981", borderColor: "#059669" },
  TERAPIA: { backgroundColor: "#f59e0b", borderColor: "#d97706" },
  ORIENTACAO_VOCACIONAL: { backgroundColor: "#8b5cf6", borderColor: "#7c3aed" },
  ENEM: { backgroundColor: "#ec4899", borderColor: "#db2777" },
  PAS: { backgroundColor: "#06b6d4", borderColor: "#0891b2" },
  OUTRO: { backgroundColor: "#6b7280", borderColor: "#4b5563" },
};

export function getAtendimentoCalendarColor(tipo?: CalendarioAtendimentosResponseTipoEnumKey | string) {
  return atendimentoTipoCalendarColor[tipo ?? "OUTRO"] ?? atendimentoTipoCalendarColor.OUTRO;
}
