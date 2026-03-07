export const EMPLOYEE_ROLE_OPTIONS = [
  { value: "EMPLOYEE", label: "Colaborador" },
  { value: "ADMIN", label: "Administrador" },
] as const

export const EMPLOYEE_ROLE_LABELS = {
  ADMIN: "Administrador",
  EMPLOYEE: "Colaborador",
  STUDENT: "Aluno",
  PARENT: "Responsável",
} as const
