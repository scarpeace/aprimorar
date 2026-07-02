"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { FormProvider, useForm } from "react-hook-form";
import type { ColaboradorResponseDTO } from "@/lib/api/generated/types/ColaboradorResponseDTO";
import { Button } from "@/components/ui/Button";
import { DateInput } from "@/components/ui/forms/DateInput";
import { MaskedInput } from "@/components/ui/forms/MaskedInput";
import { SelectInput } from "@/components/ui/forms/SelectInput";
import { TextInput } from "@/components/ui/forms/TextInput";
import { useColaboradorMutations } from "@/hooks/use-colaborador-mutations";
import { BRAZILIAN_STATES } from "@/lib/constants/address-constants";
import { colaboradorFormSchema, type ColaboradorFormData } from "@/lib/validators/colaborador-form-schema";

const FUNCAO_OPTIONS: { value: string; label: string }[] = [
  { value: "PROFESSOR", label: "Professor" },
  { value: "ADMINISTRATIVO", label: "Administrativo" },
  { value: "TERAPEUTA", label: "Terapeuta" },
  { value: "MENTOR", label: "Mentor" },
];

type ColaboradorFormProps = {
  initialData?: ColaboradorResponseDTO;
  onSuccess: () => void;
  onCancel: () => void;
};

export function ColaboradorForm({ initialData, onSuccess, onCancel }: Readonly<ColaboradorFormProps>) {
  const { createColaborador, updateColaborador } = useColaboradorMutations();
  const isEditMode = !!initialData?.id;

  const methods = useForm<ColaboradorFormData>({
    resolver: zodResolver(colaboradorFormSchema),
    mode: "onBlur",
    defaultValues: {
      nome: initialData?.nome ?? "",
      dataNascimento: initialData?.dataNascimento ?? "",
      pix: initialData?.pix ?? "",
      telefone: initialData?.telefone ?? "",
      cpf: initialData?.cpf ?? "",
      email: initialData?.email ?? "",
      funcao: initialData?.funcao ?? "PROFESSOR",
      endereco: {
        rua: initialData?.endereco.rua ?? "",
        numero: initialData?.endereco.numero ?? "",
        complemento: initialData?.endereco.complemento ?? "",
        bairro: initialData?.endereco.bairro ?? "",
        cidade: initialData?.endereco.cidade ?? "",
        estado: initialData?.endereco.estado ?? "",
        cep: initialData?.endereco.cep ?? "",
      },
    },
  });

  const isPending = createColaborador.isPending || updateColaborador.isPending;

  const onSubmit = methods.handleSubmit((data) => {
    if (isEditMode && initialData?.id) {
      updateColaborador.mutate(
        { colaboradorId: initialData.id, data },
        {
          onSuccess,
        },
      );
      return;
    }

    createColaborador.mutate(
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
            <h4 className="text-base font-bold text-base-content">Informações do colaborador</h4>
            <p className="text-sm text-base-content/60">Preencha os dados principais do colaborador.</p>
          </div>

          <div className="grid gap-4 md:grid-cols-2">
            <TextInput name="nome" label="Nome" disabled={isPending} />
            <TextInput name="email" type="email" label="E-mail" disabled={isPending} />
            <DateInput name="dataNascimento" label="Data de nascimento" disabled={isPending} />
            <SelectInput name="funcao" label="Função" options={FUNCAO_OPTIONS} disabled={isPending} />
            <MaskedInput name="cpf" label="CPF" mask="000.000.000-00" placeholder="000.000.000-00" disabled={isPending} />
            <MaskedInput
              name="telefone"
              label="Telefone"
              mask="(00) 00000-0000"
              placeholder="(00) 00000-0000"
              disabled={isPending}
            />
            <div className="md:col-span-2">
              <TextInput name="pix" label="PIX" disabled={isPending} />
            </div>
          </div>
        </section>

        <section className="space-y-4">
          <div>
            <h4 className="text-base font-bold text-base-content">Endereço</h4>
            <p className="text-sm text-base-content/60">Mantenha o endereço sincronizado com o cadastro.</p>
          </div>

          <div className="grid gap-4 md:grid-cols-2">
            <div className="md:col-span-2">
              <TextInput name="endereco.rua" label="Rua" disabled={isPending} />
            </div>

            <TextInput name="endereco.numero" label="Número" disabled={isPending} />
            <TextInput name="endereco.complemento" label="Complemento" disabled={isPending} />
            <TextInput name="endereco.bairro" label="Bairro" disabled={isPending} />
            <TextInput name="endereco.cidade" label="Cidade" disabled={isPending} />
            <SelectInput
              name="endereco.estado"
              label="Estado"
              options={BRAZILIAN_STATES}
              placeholder="Selecione"
              disabled={isPending}
            />
            <MaskedInput name="endereco.cep" label="CEP" mask="00000-000" placeholder="00000-000" disabled={isPending} />
          </div>
        </section>

        <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <Button type="button" variant="outline" disabled={isPending} onClick={onCancel}>
            Cancelar
          </Button>
          <Button type="submit" disabled={isPending}>
            {isPending ? "Salvando..." : isEditMode ? "Salvar alterações" : "Cadastrar colaborador"}
          </Button>
        </div>
      </form>
    </FormProvider>
  );
}
