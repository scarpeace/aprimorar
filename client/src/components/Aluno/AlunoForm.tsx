import { zodResolver } from "@hookform/resolvers/zod";
import { FormProvider, useForm } from "react-hook-form";

import { Button } from "@/components/Ui/Button.tsx";
import { useListResponsaveis, type AlunoResponseDTO } from "@/kubb";
import { addressConstants } from "@/utils/constants/address-constants.ts";
import { formatDateForInput } from "@/utils/date-utils.ts";
import { useAlunoMutations } from "../../services/use-aluno-mutations.ts";
import { alunoFormSchema, type AlunoFormSchema } from "../../utils/zod/aluno-form-schema.ts";
import { SelectInput } from "@/components/Forms/SelectInput.tsx";
import { TextInput } from "@/components/Forms/TextInput.tsx";
import { MaskedInput } from "@/components/Forms/MaskedInput.tsx";
import { UserPlus } from "lucide-react";

type AlunoFormProps = {
  initialData?: AlunoResponseDTO;
  onSuccess: () => void;
  onCancel: () => void;
};

export function AlunoForm({ initialData, onSuccess, onCancel }: Readonly<AlunoFormProps>) {
  const { createAluno, updateAluno } = useAlunoMutations();

  const responsaveisList = useListResponsaveis();
  const responsavelOptions = responsaveisList.data?.map((item) => ({
    value: item.id,
    label: item.nome,
  })) ?? [];

  const estadosOptions = Object.values(addressConstants).map((estado) => ({
    value: estado,
    label: estado,
  }));

  const isEditMode = !!initialData;
  const methods = useForm<AlunoFormSchema>({
    resolver: zodResolver(alunoFormSchema),
    mode: "onBlur",
    defaultValues: {
      nome: initialData?.nome,
      cpf: initialData?.cpf,
      dataNascimento: formatDateForInput(initialData?.dataNascimento),
      telefone: initialData?.telefone,
      email: initialData?.email,
      escola: initialData?.escola,
      responsavelId: initialData?.responsavelId,
      endereco: {
        rua: initialData?.endereco?.rua,
        numero: initialData?.endereco?.numero,
        complemento: initialData?.endereco?.complemento,
        bairro: initialData?.endereco?.bairro,
        cidade: initialData?.endereco?.cidade,
        estado: initialData?.endereco?.estado.toString(),
        cep: initialData?.endereco?.cep,
      },
    },
  });

  const onSubmit = methods.handleSubmit((data) => {
    if (isEditMode && initialData?.id) {
      updateAluno.mutate({ alunoId: initialData.id, data },
        { onSuccess },
      );
      return;
    }

    createAluno.mutate({ data },
      { onSuccess },
    );
  }, (error) => {
    console.error(error);
  });

  const isPending = createAluno.isPending || updateAluno.isPending;

  return (
    <FormProvider {...methods}>
      <form className="flex flex-col" onSubmit={onSubmit} autoComplete="off">
        <div className="grid grid-cols-1 gap-x-3 md:grid-cols-3">
          <span className="col-span-3 text-md font-bold">Informações Pessoais</span>
          <span className="col-span-3 text-xs">Não achou o responsável? Cadastre um novo
            <Button variant="success" className="p-1 h-6 w-6 ml-2"><UserPlus size={18}/></Button>
          </span>

          <SelectInput name="responsavelId" label="Responsável" options={responsavelOptions} />
          <TextInput name="nome" label="Nome" placeholder="Nome completo" type="text" />
          <MaskedInput name="dataNascimento" label="Data de Nascimento" placeholder="Ex: 01/01/1990" mask="00/00/0000" />
          <MaskedInput name="cpf" label="CPF" placeholder="Ex: 123.456.789-00" mask="000.000.000-00" />
          <MaskedInput name="telefone" label="Telefone" placeholder="Ex: (61) 99633-2332" mask="(00) 00000-0000" />
          <TextInput name="email" label="Email" placeholder="Ex: email@email.com" type="email"/>
          <TextInput name="escola" label="Escola" placeholder="Ex: Leonardo da Vinci" type="text" />

          <div className="divider col-span-3" />

          <span className="col-span-3 text-md font-bold">Endereço</span>
          <TextInput name="endereco.rua" label="Rua" placeholder="Ex: SQS 406, Bloco C" type="text" />
          <TextInput name="endereco.numero" label="Número" placeholder="Ex: 101" type="text" />
          <TextInput name="endereco.complemento" label="Complemento" placeholder="" type="text" />
          <TextInput name="endereco.bairro" label="Bairro" placeholder="Ex: Asa Sul" type="text" />
          <TextInput name="endereco.cidade" label="Cidade" placeholder="Ex: Brasília" type="text" />
          <SelectInput name="endereco.estado" label="Estado" options={estadosOptions} />
          <MaskedInput name="endereco.cep" label="CEP" placeholder="Ex: 70254-010" mask="00000-000" />
      </div>

      <div className="mt-3 flex gap-3 sm:flex-row sm:justify-end">
        <Button type="button" variant="outline" onClick={onCancel} disabled={isPending}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isPending} variant="primary">
          {isPending ? "Salvando..." : (isEditMode ? "Atualizar Aluno" : "Cadastrar Aluno")}
        </Button>
      </div>
      </form>
    </FormProvider>
  );
}
