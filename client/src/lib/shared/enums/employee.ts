export const dutyValues = ["TEACHER", "ADM", "THERAPIST", "MENTOR"] as const

export type Duty = (typeof dutyValues)[number]
