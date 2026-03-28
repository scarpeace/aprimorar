import type { ParentResponseDTO } from "@/kubb";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { parentInputSchema, type ParentInputSchema, type ParentResponseSchema } from "./parentSchema";

export function useParentForm(parent?: ParentResponseSchema) {

  const form = useForm<ParentInputSchema>({
    resolver: zodResolver(parentInputSchema),
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
