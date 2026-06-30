import { atendimentoResponseStatusEnum, atendimentoResponseTipoEnum } from "@/lib/api/generated/types/AtendimentoResponse";

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
