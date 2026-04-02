import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { studentCreateSchema, type StudentCreateSchema } from "./studentSchema";
import type { StudentResponseDTO } from "@/kubb";

export function useStudentForm(student?: StudentResponseDTO) {

  const form = useForm<StudentCreateSchema>({
    resolver: zodResolver(studentCreateSchema),
    mode: "onBlur",
    values: {
      name: student?.name ?? "",
      birthdate: student?.birthdate ?? "",
      cpf: student?.cpf ?? "",
      contact: student?.contact ?? "",
      email: student?.email ?? "",
      school: student?.school ?? "",
      address: {
        street: student?.address?.street ?? "",
        number: student?.address?.number ?? "",
        complement: student?.address?.complement ?? "N/A",
        district: student?.address?.district ?? "",
        city: student?.address?.city ?? "",
        state: student?.address?.state ?? "DF",
        zip: student?.address?.zip ?? "",
      },
      parent: {
        name: student?.parent?.name ?? "",
        contact: student?.parent?.contact ?? "",
        email: student?.parent?.email ?? "",
        cpf: student?.parent?.cpf ?? "",
      },
    },
  });

  const registerWithMask = useHookFormMask(form.register);

  return {
    ...form,
    errors: form.formState.errors,
    registerWithMask,
  };
}
