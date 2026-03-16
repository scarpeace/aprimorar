export const eventContentValues = [
  "AULA",
  "MENTORIA",
  "TERAPIA",
  "ORIENTACAO_VOCACIONAL",
  "ENEM",
  "PAS",
  "OUTRO",
] as const

export type EventContent = (typeof eventContentValues)[number]

export const eventContentLabels: Record<EventContent, string> = {
  AULA: "AULA",
  TERAPIA: "TERAPIA",
  MENTORIA: "MENTORIA",
  ORIENTACAO_VOCACIONAL: "OR. VOCAC",
  ENEM: "ENEM",
  PAS: "PAS",
  OUTRO: "OUTRO",
}
