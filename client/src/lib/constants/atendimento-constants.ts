export const tipoAtendimentoLabels = {
  AULA: "Aula",
  MENTORIA: "Mentoria",
  TERAPIA: "Terapia",
  ORIENTACAO_VOCACIONAL: "Orientação Vocacional",
  ENEM: "Enem",
  PAS: "PAS",
  OUTRO: "Outro",
} as const;

export const atendimentoTipoOptions = [
  { value: "", label: "Todos os tipos" },
  ...Object.entries(tipoAtendimentoLabels).map(([value, label]) => ({
    value,
    label,
  })),
];

export const statusAtendimentoLabels = {
  AGENDADO: "Agendado",
  CONCLUIDO: "Concluído",
  CANCELADO: "Cancelado",
} as const;

export const atendimentoStatusOptions = [
  { value: "", label: "Todos os status" },
  ...Object.entries(statusAtendimentoLabels).map(([value, label]) => ({
    value,
    label,
  })),
];

export const statusCobrancaOptions = [
  { value: "", label: "Todas as cobranças" },
  { value: "PENDENTE", label: "Pendente" },
  { value: "PAGO", label: "Pago" },
] as const;

export const atendimentoTipoBadgeClass = {
  AULA: "badge-primary",
  MENTORIA: "badge-info",
  TERAPIA: "badge-success",
  ORIENTACAO_VOCACIONAL: "badge-secondary",
  ENEM: "badge-error",
  PAS: "badge-accent",
  OUTRO: "badge-ghost",
} as const;

export const atendimentoTipoCalendarClass = {
  AULA: "atendimento-event--aula",
  MENTORIA: "atendimento-event--mentoria",
  TERAPIA: "atendimento-event--terapia",
  ORIENTACAO_VOCACIONAL: "atendimento-event--orientacao",
  ENEM: "atendimento-event--enem",
  PAS: "atendimento-event--pas",
  OUTRO: "atendimento-event--outro",
} as const;

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
