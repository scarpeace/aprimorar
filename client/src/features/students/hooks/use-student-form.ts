import type { StudentResponseDTO } from "@/kubb";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { studentInputSchema, type StudentInputSchema, type StudentResponseSchema } from "./studentSchema";

export function useStudentForm(student?: StudentResponseSchema) {

  const form = useForm<StudentInputSchema>({
    resolver: zodResolver(studentInputSchema),
    mode: "onBlur",
    values: {
      name: student?.name ?? "",
      birthdate: student?.birthdate ?? "",
      cpf: student?.cpf ?? "",
      contact: student?.contact ?? "",
      email: student?.email ?? "",
      school: student?.school ?? "",
      address: {
        street: student?.address.street ?? "",
        number: student?.address.number ?? "",
        complement: student?.address.complement ?? "",
        district: student?.address.district ?? "",
        city: student?.address.city ?? "",
        state: student?.address.state ?? "",
        zip: student?.address.zip ?? "",
      },
      parent: {
        name: student?.parent.name ?? "",
        contact: student?.parent.contact ?? "",
        email: student?.parent.email ?? "",
        cpf: student?.parent.cpf ?? "",
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
