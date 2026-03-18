export const dutyValues = ["TEACHER", "ADM", "THERAPIST", "MENTOR", "SYSTEM"] as const

export type Duty = (typeof dutyValues)[number]
