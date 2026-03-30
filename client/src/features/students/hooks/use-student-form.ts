import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, type UseFormReturn } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { studentInputSchema, type StudentInputSchema } from "./studentSchema";
import type { StudentResponseDTO } from "@/kubb";
import { formatCpf } from "@/lib/utils/formatter";

//TODO: testar o fluxo completo de criação de aluno depois que arrumar o parent
//TODO: em tempo, isso é necessario \/?
type UseStudentFormReturn = UseFormReturn<StudentInputSchema> & {
  errors: UseFormReturn<StudentInputSchema>["formState"]["errors"];
  registerWithMask: ReturnType<typeof useHookFormMask>;
};

export function useStudentForm(student?: StudentResponseDTO) : UseStudentFormReturn {

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
        street: student?.address?.street ?? "",
        number: student?.address?.number ?? "",
        complement: student?.address?.complement ?? "",
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
