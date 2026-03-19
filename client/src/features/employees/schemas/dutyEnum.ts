export const dutyValues = [
  "TEACHER",
  "ADM",
  "THERAPIST",
  "MENTOR",
  "SYSTEM",
] as const;

export type Duty = typeof dutyValues[number];

export const dutyLabels = {
  TEACHER: "Professor(a)",
  ADM: "Administrativo",
  THERAPIST: "Terapeuta",
  MENTOR: "Mentor(a)",
  SYSTEM: "Sistema",
} as const satisfies Record<Duty, string>;
