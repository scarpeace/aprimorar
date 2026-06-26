import { Button } from "@/components/Ui/Button.tsx";
import {
  colaboradorRequestDTOFuncaoEnum,
  type ColaboradorResponseDTO,
} from "@/kubb";
import { addressConstants } from "@/utils/constants/address-constants.ts";
import { formatDateForInput } from "@/utils/date-utils.ts";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormProvider, useForm } from "react-hook-form";
import { useColaboradorMutations } from "../../services/use-colaborador-mutations.ts";
import {
  colaboradorFormSchema,
  type ColaboradorFormSchema,
} from "../../utils/zod/colaborador-form-schema.ts";
import { TextInput } from "@/components/Forms/TextInput.tsx";
import { MaskedInput } from "@/components/Forms/MaskedInput.tsx";
import { SelectInput } from "@/components/Forms/SelectInput.tsx";

type ColaboradorFormProps = {
  initialData?: ColaboradorResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
};

export function ColaboradorForm({ initialData, onSuccess, onCancel }: Readonly<ColaboradorFormProps>) {
  const { createColaborador, updateColaborador } = useColaboradorMutations();

  const funcoesOptions = Object.entries(colaboradorRequestDTOFuncaoEnum).map(([value, label]) => ({ value, label }));
  const estadosOptions = Object.values(addressConstants).map((estado) => ({
    value: estado,
    label: estado,
  }));

  const isEditMode = !!initialData;
  const methods = useForm<ColaboradorFormSchema>({
    resolver: zodResolver(colaboradorFormSchema),
    defaultValues: {
      nome: initialData?.nome ?? "",
      email: initialData?.email ?? "",
      telefone: initialData?.telefone ?? "",
      cpf: initialData?.cpf ?? "",
      pix: initialData?.pix ?? "",
      dataNascimento: formatDateForInput(initialData?.dataNascimento),
      funcao: initialData?.funcao ?? colaboradorRequestDTOFuncaoEnum.PROFESSOR,
      endereco: {
        rua: initialData?.endereco?.rua ?? "",
        numero: initialData?.endereco?.numero ?? "",
        complemento: initialData?.endereco?.complemento ?? "",
        bairro: initialData?.endereco?.bairro ?? "",
        cidade: initialData?.endereco?.cidade ?? "",
        estado: initialData?.endereco?.estado ?? "",
        cep: initialData?.endereco?.cep ?? "",
      },
    },
    mode: "onBlur",
  });


  const onSubmit = methods.handleSubmit((data) => {
    if (isEditMode && initialData?.id) {
      updateColaborador.mutate({ colaboradorId: initialData.id, data },
        { onSuccess },
      );
      return;
    }
    createColaborador.mutate({ data },
      { onSuccess });
  });

  const isPending = createColaborador.isPending || updateColaborador.isPending;

  return (
    <FormProvider {...methods}>
    <form className="flex flex-col" onSubmit={onSubmit} autoComplete="off">
        <div className="grid grid-cols-1 gap-x-3 md:grid-cols-3">


          <span className="col-span-3 text-md font-bold">Informações pessoais</span>
          <TextInput name="nome" label="Nome" placeholder="Nome completo" type="text" />
          <MaskedInput name="dataNascimento" label="Data de Nascimento" placeholder="Ex: 01/01/1990" mask="00/00/0000" />
          <MaskedInput name="cpf" label="CPF" placeholder="Ex: 123.456.789-00" mask="000.000.000-00" />
          <MaskedInput name="telefone" label="Telefone" placeholder="Ex: (61) 99633-2332" mask="(00) 00000-0000" />
          <TextInput name="email" label="Email" placeholder="Ex: email@email.com" type="email"/>
          <TextInput name="pix" label="Pix" placeholder="Ex: pix@pix.com" type="text" />
          <SelectInput name="funcao" label="Função" options={funcoesOptions} />

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
        <div className="mt-4">
          <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
            <Button type="button" variant="ghost" onClick={onCancel} disabled={isPending}>
              Cancelar
            </Button>
            <Button type="submit" disabled={isPending} variant="primary">
              {isPending ? "Salvando..." : (isEditMode ? "Atualizar Colaborador" : "Salvar Colaborador")}
            </Button>
          </div>
        </div>
      </form>
    </FormProvider>
  );
}
