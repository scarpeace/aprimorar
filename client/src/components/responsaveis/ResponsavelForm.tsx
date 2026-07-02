"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { FormProvider, useForm } from "react-hook-form";
import type { ResponsavelResponseDTO } from "@/lib/api/generated/types/ResponsavelResponseDTO";
import { Button } from "@/components/ui/Button";
import { DateInput } from "@/components/ui/forms/DateInput";
import { MaskedInput } from "@/components/ui/forms/MaskedInput";
import { TextInput } from "@/components/ui/forms/TextInput";
import { useResponsavelMutations } from "@/hooks/use-responsavel-mutations";
import { responsavelFormSchema, type ResponsavelFormData } from "@/lib/validators/responsavel-form-schema";

type ResponsavelFormProps = {
  initialData?: ResponsavelResponseDTO;
  onSuccess: () => void;
  onCancel: () => void;
};

export function ResponsavelForm({ initialData, onSuccess, onCancel }: Readonly<ResponsavelFormProps>) {
  const { createResponsavel, updateResponsavel } = useResponsavelMutations();
  const isEditMode = !!initialData?.id;

  const methods = useForm<ResponsavelFormData>({
    resolver: zodResolver(responsavelFormSchema),
    mode: "onBlur",
    defaultValues: {
      nome: initialData?.nome ?? "",
      email: initialData?.email ?? "",
      telefone: initialData?.telefone ?? "",
      dataNascimento: initialData?.dataNascimento ?? "",
      cpf: initialData?.cpf ?? "",
    },
  });

  const isPending = createResponsavel.isPending || updateResponsavel.isPending;

  const onSubmit = methods.handleSubmit((data) => {
    if (isEditMode && initialData?.id) {
      updateResponsavel.mutate(
        { responsavelId: initialData.id, data },
        {
          onSuccess,
        },
      );
      return;
    }

    createResponsavel.mutate(
      { data },
      {
        onSuccess,
      },
    );
  });

  return (
    <FormProvider {...methods}>
      <form className="flex flex-col gap-6" autoComplete="off" onSubmit={onSubmit}>
        <section className="space-y-4">
          <div>
            <h4 className="text-base font-bold text-base-content">Informações do responsável</h4>
            <p className="text-sm text-base-content/60">Preencha os dados principais do responsável.</p>
          </div>

          <div className="grid gap-4 md:grid-cols-2">
            <TextInput name="nome" label="Nome" disabled={isPending} />
            <TextInput name="email" type="email" label="E-mail" disabled={isPending} />
            <MaskedInput name="telefone" label="Telefone" mask="(00) 00000-0000" placeholder="(00) 00000-0000" disabled={isPending} />
            <DateInput name="dataNascimento" label="Data de nascimento" disabled={isPending} />
            <MaskedInput name="cpf" label="CPF" mask="000.000.000-00" placeholder="000.000.000-00" disabled={isPending} />
          </div>
        </section>

        <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <Button type="button" variant="outline" disabled={isPending} onClick={onCancel}>
            Cancelar
          </Button>
          <Button type="submit" disabled={isPending}>
            {isPending ? "Salvando..." : isEditMode ? "Salvar alterações" : "Cadastrar responsável"}
          </Button>
        </div>
      </form>
    </FormProvider>
  );
}
