import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useHookFormMask } from "use-mask-input";
import { Button } from "@/components/ui/button";
import { TriangleAlert } from "lucide-react";
import type { ResponsavelResponseDTO } from "@/kubb";
import { responsavelFormSchema, type ResponsavelFormSchema } from "../lib/parentFormSchema.ts";
import { useResponsavelMutations } from "../hooks/use-responsavel-mutations";
import { toast } from "sonner";
import { formatDateForInput } from "@/lib/utils/formatter";
import { getFriendlyErrorMessage } from "@/lib/shared/api.ts";

interface ResponsavelFormProps {
  initialData?: ResponsavelResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
}

export function ResponsavelForm({ initialData, onSuccess, onCancel }: ResponsavelFormProps) {
  const { createParent, updateParent } = useResponsavelMutations();
  const isEditMode = !!initialData;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ResponsavelFormSchema>({
    resolver: zodResolver(responsavelFormSchema),
    defaultValues: {
      name: initialData?.name ?? "",
      email: initialData?.email ?? "",
      contact: initialData?.contact ?? "",
      birthdate: formatDateForInput(initialData?.birthdate),
      pix: initialData?.pix ?? "",
      cpf: initialData?.cpf ?? "",
    },
    mode: "onBlur",
  });

  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: ResponsavelFormSchema) => {
    if (isEditMode && initialData.parentId) {
      updateParent.mutate(
        { responsavelId: initialData.parentId, data },
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
          <legend className="fieldset-legend">Data de Nascimento</legend>
          <input
            type="text"
            className="input w-full"
            {...registerWithMask("birthdate", ["##/##/####"])}
            placeholder="Ex: 01/01/1990"
          />
          {errors?.birthdate && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.birthdate.message}
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

        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Pix</legend>
          <input
            type="text"
            className="input w-full"
            {...register("pix")}
            placeholder="Chave Pix do responsável"
          />
          {errors?.pix && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.pix.message}
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
