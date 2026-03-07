import type { CreateStudentInput, StudentResponse } from "@/lib/schemas"

export type StudentCreateParentMode = "existing" | "new"
export type StudentEditParentMode = "editCurrent" | "switchExisting"

function toDateInputValue(value: string | undefined) {
  if (!value) return ""
  if (value.includes("T")) {
    return value.slice(0, 10)
  }

  return value
}

export function mapStudentResponseToFormValues(student: StudentResponse): CreateStudentInput {
  return {
    name: student.name,
    birthdate: toDateInputValue(student.birthdate),
    cpf: student.cpf,
    contact: student.contact,
    email: student.email,
    school: student.school,
    address: {
      street: student.address?.street ?? "",
      number: student.address?.number ?? "",
      complement: student.address?.complement ?? "",
      district: student.address?.district ?? "",
      city: student.address?.city ?? "",
      state: student.address?.state ?? "",
      zip: student.address?.zip ?? "",
    },
    parentId: student.parent?.id,
    parent: student.parent
      ? {
          name: student.parent.name,
          email: student.parent.email,
          contact: student.parent.contact,
          cpf: student.parent.cpf,
        }
      : undefined,
  }
}

export function buildCreateStudentPayload(data: CreateStudentInput, parentMode: StudentCreateParentMode): CreateStudentInput {
  return parentMode === "existing"
    ? { ...data, parent: undefined }
    : { ...data, parentId: undefined }
}

export function buildEditStudentPayload(data: CreateStudentInput, parentMode: StudentEditParentMode): CreateStudentInput {
  return parentMode === "switchExisting"
    ? { ...data, parent: undefined }
    : { ...data, parentId: undefined }
}
