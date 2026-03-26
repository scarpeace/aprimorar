import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import type { ParentResponseDTOSchema } from "@/kubb";
import { parentFormSchema, type ParentFormInput } from "@/features/parents/schemas/parentFormSchema";

export function useParentForm(parent?: ParentResponseDTOSchema) {

  const form = useForm<ParentFormInput>({
    resolver: zodResolver(parentFormSchema),
    mode: "onBlur",
    values: {
      name: parent?.name ?? "",
      cpf: parent?.cpf ?? "",
      contact: parent?.contact ?? "",
      email: parent?.email ?? "",
    },
  });

  const registerWithMask = useHookFormMask(form.register);

  return {
    ...form,
    errors: form.formState.errors,
    registerWithMask,
  };
}
