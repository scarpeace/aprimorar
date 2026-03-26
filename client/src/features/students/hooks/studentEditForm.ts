import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { studentFormSchema } from "../schemas/studentFormSchema";
import type { StudentApiResponse, StudentFormInput } from "../types";

export function useStudentEditForm(student?: StudentApiResponse) {
  const form = useForm<StudentFormInput>({
    resolver: zodResolver(studentFormSchema),
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
        complement: student?.address?.complement ?? "",
        district: student?.address?.district ?? "",
        city: student?.address?.city ?? "",
        state: student?.address?.state ?? "DF",
        zip: student?.address?.zip ?? "",
      },
      parentId: student?.parent?.parentId ?? "",
    },
  });

  const registerWithMask = useHookFormMask(form.register);
  const selectedParentId = form.watch("parentId");

  return {
    ...form,
    errors: form.formState.errors,
    registerWithMask,
    selectedParentId,
  };
}
