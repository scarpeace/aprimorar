import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useHookFormMask } from "use-mask-input";
import { Button } from "@/components/ui/button";
import { TriangleAlert } from "lucide-react";
import type { ResponsavelResponseDTO } from "@/kubb";
import { responsavelFormSchema, type ResponsavelFormSchema } from "../lib/responsavel-form-schema.ts";
import { useResponsavelMutations } from "../hooks/use-responsavel-mutations";
import { formatDateForInput } from "@/lib/utils/formatter";
import type { ReactNode } from "react";

function FieldErrorMessage({ children }: { children: ReactNode }) {
  if (!children) return null;

  return (
    <p className="label text-error">
      <TriangleAlert className="h-3 w-3" />
      {children}
    </p>
  );
}

interface ResponsavelFormProps {
  initialData?: ResponsavelResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
}

export function ResponsavelForm({ initialData, onSuccess, onCancel }: ResponsavelFormProps) {
  const { createResponsavel, updateResponsavel } = useResponsavelMutations();
  const isEditMode = !!initialData;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ResponsavelFormSchema>({
    resolver: zodResolver(responsavelFormSchema),
    defaultValues: {
      nome: initialData?.nome ?? "",
      email: initialData?.email ?? "",
      telefone: initialData?.telefone ?? "",
      dataNascimento: formatDateForInput(initialData?.dataNascimento),
      cpf: initialData?.cpf ?? "",
    },
    mode: "onBlur",
  });

  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: ResponsavelFormSchema) => {
    if (isEditMode && initialData.id) {
      updateResponsavel.mutate(
        { responsavelId: initialData.id, data },
        {
          onSuccess: () => onSuccess(),
        }
      );
    } else {
      createResponsavel.mutate(
        { data },
        {
          onSuccess: () => onSuccess(),
        }
      );
    }
  });

  const isPending = createResponsavel.isPending || updateResponsavel.isPending;

  return (
    <form className="flex flex-col" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 md:grid-cols-2">
        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Nome</legend>
          <input
            type="text"
            className="input w-full"
            {...register("nome")}
            placeholder="Nome completo"
          />
          <FieldErrorMessage>{errors.nome?.message}</FieldErrorMessage>
        </fieldset>

        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Email</legend>
          <input
            type="text"
            className="input w-full"
            {...register("email")}
            placeholder="email@email.com"
          />
          <FieldErrorMessage>{errors.email?.message}</FieldErrorMessage>
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Data de Nascimento</legend>
          <input
            type="text"
            className="input w-full"
            {...registerWithMask("dataNascimento", ["##/##/####"])}
            placeholder="Ex: 01/01/1990"
          />
          <FieldErrorMessage>{errors.dataNascimento?.message}</FieldErrorMessage>
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Contato</legend>
          <input
            type="text"
            className="input w-full"
            placeholder="Ex: (61) 99633-2332"
            {...registerWithMask("telefone", ["(##) #####-####", "(##) ####-####"])}
          />
          <FieldErrorMessage>{errors.telefone?.message}</FieldErrorMessage>
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
          <FieldErrorMessage>{errors.cpf?.message}</FieldErrorMessage>
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
