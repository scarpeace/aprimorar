"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { FormProvider, useForm } from "react-hook-form";
import type { AlunoResponseDTO } from "@/lib/api/generated/types/AlunoResponseDTO";
import { useListResponsaveis } from "@/lib/api/generated/hooks/responsavel/useListResponsaveis";
import { Button } from "@/components/ui/Button";
import { DateInput } from "@/components/ui/forms/DateInput";
import { MaskedInput } from "@/components/ui/forms/MaskedInput";
import { SelectInput } from "@/components/ui/forms/SelectInput";
import { TextInput } from "@/components/ui/forms/TextInput";
import { useAlunoMutations } from "@/hooks/use-aluno-mutations";
import { BRAZILIAN_STATES } from "@/lib/constants/address-constants";
import { alunoFormSchema, type AlunoFormData } from "@/lib/validators/aluno-form-schema";

type AlunoFormProps = {
  initialData?: AlunoResponseDTO;
  onSuccess: () => void;
  onCancel: () => void;
};

export function AlunoForm({ initialData, onSuccess, onCancel }: Readonly<AlunoFormProps>) {
  const { createAluno, updateAluno } = useAlunoMutations();
  const responsaveis = useListResponsaveis();
  const isEditMode = !!initialData?.id;

  const methods = useForm<AlunoFormData>({
    resolver: zodResolver(alunoFormSchema),
    mode: "onBlur",
    defaultValues: {
      nome: initialData?.nome ?? "",
      dataNascimento: initialData?.dataNascimento ?? "",
      cpf: initialData?.cpf ?? "",
      escola: initialData?.escola ?? "",
      telefone: initialData?.telefone ?? "",
      email: initialData?.email ?? "",
      responsavelId: initialData?.responsavelId ?? "",
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

  const isPending = createAluno.isPending || updateAluno.isPending;
  const responsavelOptions =
    responsaveis.data?.map((item) => ({
      value: item.id,
      label: item.nome,
    })) ?? [];

  const onSubmit = methods.handleSubmit((data) => {
    if (isEditMode && initialData?.id) {
      updateAluno.mutate(
        { alunoId: initialData.id, data },
        {
          onSuccess,
        },
      );
      return;
    }

    createAluno.mutate(
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
            <h4 className="text-base font-bold text-base-content">Informações pessoais</h4>
            <p className="text-sm text-base-content/60">Atualize os dados principais do aluno.</p>
          </div>

          <div className="grid gap-4 md:grid-cols-2">
            <SelectInput
              name="responsavelId"
              label="Responsável"
              options={responsavelOptions}
              placeholder="Selecione um responsável"
              disabled={isPending || responsaveis.isLoading}
            />

            <TextInput name="nome" label="Nome" disabled={isPending} />
            <DateInput name="dataNascimento" label="Data de nascimento" disabled={isPending} />
            <MaskedInput name="cpf" label="CPF" mask="000.000.000-00" placeholder="000.000.000-00" disabled={isPending} />
            <MaskedInput
              name="telefone"
              label="Telefone"
              mask="(00) 00000-0000"
              placeholder="(00) 00000-0000"
              disabled={isPending}
            />
            <TextInput name="email" type="email" label="E-mail" disabled={isPending} />

            <div className="md:col-span-2">
              <TextInput name="escola" label="Escola" disabled={isPending} />
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
            {isPending ? "Salvando..." : isEditMode ? "Salvar alterações" : "Cadastrar aluno"}
          </Button>
        </div>
      </form>
    </FormProvider>
  );
}
