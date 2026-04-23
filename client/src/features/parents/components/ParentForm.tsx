import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useHookFormMask } from "use-mask-input";
import { Button } from "@/components/ui/button";
import { TriangleAlert } from "lucide-react";
import type { ParentResponseDTO } from "@/kubb";
import { parentFormSchema, type ParentFormSchema } from "../forms/parentFormSchema";
import { useParentMutations } from "../hooks/parent-mutations";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";

interface ParentFormProps {
  initialData?: ParentResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
}

export function ParentForm({ initialData, onSuccess, onCancel }: ParentFormProps) {
  const { createParent, updateParent } = useParentMutations();
  const isEditMode = !!initialData;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ParentFormSchema>({
    resolver: zodResolver(parentFormSchema),
    defaultValues: {
      name: initialData?.name ?? "",
      email: initialData?.email ?? "",
      contact: initialData?.contact ?? "",
      cpf: initialData?.cpf ?? "",
    },
    mode: "onBlur",
  });

  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: ParentFormSchema) => {
    if (isEditMode && initialData.parentId) {
      updateParent.mutate(
        { parentId: initialData.parentId, data },
        {
          onSuccess: () => onSuccess(),
          onError: (error) => toast.error(getFriendlyErrorMessage(error)),
        }
      );
    } else {
      createParent.mutate(
        { data },
        {
          onSuccess: () => onSuccess(),
          onError: (error) => toast.error(getFriendlyErrorMessage(error)),
        }
      );
    }
  });

  const isPending = createParent.isPending || updateParent.isPending;

  return (
    <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-5">
        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Nome</legend>
          <input 
            type="text" 
            className="input w-full" 
            {...register("name")} 
            placeholder="Nome completo" 
          />
          {errors?.name && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.name.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Email</legend>
          <input 
            type="text" 
            className="input w-full" 
            {...register("email")} 
            placeholder="email@email.com" 
          />
          {errors?.email && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.email.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Contato</legend>
          <input
            type="text"
            className="input w-full"
            placeholder="Ex: (61) 99633-2332"
            {...registerWithMask("contact", ["(##) #####-####", "(##) ####-####"])}
          />
          {errors?.contact && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.contact.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">CPF</legend>
          <input
            type="text"
            className="input w-full"
            disabled={isEditMode}
            placeholder="Ex: 123.456.789-00"
            {...registerWithMask("cpf", ["###.###.###-##"])}
          />
          {errors?.cpf && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.cpf.message}
            </p>
          )}
        </fieldset>
      </div>

      <div className="mt-4 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
        <Button type="button" variant="ghost" onClick={onCancel}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isPending} variant="primary">
          {isPending ? "Salvando..." : (isEditMode ? "Atualizar Responsável" : "Salvar Responsável")}
        </Button>
      </div>
    </form>
  );
}
