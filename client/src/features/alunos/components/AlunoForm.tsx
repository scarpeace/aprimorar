import { zodResolver } from "@hookform/resolvers/zod";
import { TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";

import { Button } from "@/components/ui/button";
import { ResponsavelSelectDropdown } from "@/features/responsaveis/components/ResponsavelSelectDropdown";
import type { AlunoResponseDTO } from "@/kubb";
import { brazilianStates } from "@/lib/utils/brazilianStates";
import { formatDateForInput } from "@/lib/utils/formatter";
import { useAlunoMutations } from "../hooks/use-aluno-mutations";
import { alunoFormSchema, type AlunoFormSchema } from "../lib/aluno-form-schema";

type AlunoFormProps = {
  initialData?: AlunoResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
};

function FieldErrorMessage({ message }: Readonly<{ message?: string }>) {
  if (!message) {
    return null;
  }

  return (
    <p className="label text-error">
      <TriangleAlert className="h-3 w-3" />
      {message}
    </p>
  );
}

export function AlunoForm({ initialData, onSuccess, onCancel }: Readonly<AlunoFormProps>) {
  const { createAluno, updateAluno } = useAlunoMutations();
  const isEditMode = !!initialData;

  const { register, control, handleSubmit, formState: { errors } } = useForm<AlunoFormSchema>({
    resolver: zodResolver(alunoFormSchema),
    mode: "onBlur",
    defaultValues: {
      nome: initialData?.nome ?? "",
      cpf: initialData?.cpf ?? "",
      dataNascimento: formatDateForInput(initialData?.dataNascimento),
      telefone: initialData?.telefone ?? "",
      email: initialData?.email ?? "",
      escola: initialData?.escola ?? "",
      responsavelId: initialData?.responsavelId ?? "",
      endereco: {
        rua: initialData?.endereco?.rua ?? "",
        numero: initialData?.endereco?.numero ?? "",
        complemento: initialData?.endereco?.complemento ?? "N/A",
        bairro: initialData?.endereco?.bairro ?? "",
        cidade: initialData?.endereco?.cidade ?? "",
        estado: initialData?.endereco?.estado ?? brazilianStates.DF,
        cep: initialData?.endereco?.cep ?? "",
      },
    },
  });

  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data) => {
    if (isEditMode && initialData?.id) {
      updateAluno.mutate(
        { alunoId: initialData.id, data },
        { onSuccess },
      );
      return;
    }

    createAluno.mutate({ data }, { onSuccess });
  });

  const isPending = createAluno.isPending || updateAluno.isPending;

  return (
    <form className="flex flex-col" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 md:grid-cols-3">
        <ResponsavelSelectDropdown
          className="col-span-3"
          label="Responsável"
          control={control}
          error={errors.responsavelId?.message}
        />

        <fieldset className="fieldset mr-3 md:col-span-2">
          <legend className="fieldset-legend">Nome</legend>
          <input type="text" className="input w-full" {...register("nome")} placeholder="Nome completo" />
          <FieldErrorMessage message={errors.nome?.message} />
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Data de Nascimento</legend>
          <input
            type="text"
            className="input w-full"
            {...registerWithMask("dataNascimento", ["##/##/####"])}
            placeholder="Ex: 01/01/1990"
          />
          <FieldErrorMessage message={errors.dataNascimento?.message} />
        </fieldset>

        <fieldset className="fieldset mr-2">
          <legend className="fieldset-legend">CPF</legend>
          <input
            type="text"
            className="input w-full"
            disabled={isEditMode}
            placeholder="Ex: 123.456.789-00"
            {...registerWithMask("cpf", ["###.###.###-##"])}
          />
          <FieldErrorMessage message={errors.cpf?.message} />
        </fieldset>

        <fieldset className="fieldset mr-3">
          <legend className="fieldset-legend">Telefone</legend>
          <input
            type="text"
            className="input w-full"
            placeholder="Ex: (61) 99633-2332"
            {...registerWithMask("telefone", ["(##) #####-####", "(##) ####-####"])}
          />
          <FieldErrorMessage message={errors.telefone?.message} />
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Email</legend>
          <input type="text" className="input w-full" {...register("email")} placeholder="email@email.com" />
          <FieldErrorMessage message={errors.email?.message} />
        </fieldset>

        <fieldset className="fieldset md:col-span-3">
          <legend className="fieldset-legend">Escola</legend>
          <input type="text" className="input w-full" {...register("escola")} placeholder="Ex: Leonardo da Vinci" />
          <FieldErrorMessage message={errors.escola?.message} />
        </fieldset>

        <div className="divider col-span-3 m-0" />

        <fieldset className="fieldset mr-3 md:col-span-2">
          <legend className="fieldset-legend">Rua</legend>
          <input type="text" className="input w-full" {...register("endereco.rua")} placeholder="Ex: SQS 406, Bloco C" />
          <FieldErrorMessage message={errors.endereco?.rua?.message} />
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Número</legend>
          <input type="text" className="input w-full" {...register("endereco.numero")} placeholder="Ex: 101" />
          <FieldErrorMessage message={errors.endereco?.numero?.message} />
        </fieldset>

        <fieldset className="fieldset mr-3">
          <legend className="fieldset-legend">Bairro</legend>
          <input type="text" className="input w-full" {...register("endereco.bairro")} placeholder="Ex: Asa Sul" />
          <FieldErrorMessage message={errors.endereco?.bairro?.message} />
        </fieldset>

        <fieldset className="fieldset mr-3">
          <legend className="fieldset-legend">Complemento</legend>
          <input type="text" className="input w-full" {...register("endereco.complemento")} placeholder="Ex: Apto 101" />
          <FieldErrorMessage message={errors.endereco?.complemento?.message} />
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Cidade</legend>
          <input type="text" className="input w-full" {...register("endereco.cidade")} placeholder="Ex: Brasília" />
          <FieldErrorMessage message={errors.endereco?.cidade?.message} />
        </fieldset>

        <fieldset className="fieldset mr-3">
          <legend className="fieldset-legend">Estado</legend>
          <select className="select select-bordered w-full" {...register("endereco.estado")}>
            {Object.values(brazilianStates).map((state) => (
              <option key={state} value={state}>
                {state}
              </option>
            ))}
          </select>
          <FieldErrorMessage message={errors.endereco?.estado?.message} />
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">CEP</legend>
          <input
            type="text"
            className="input w-full"
            {...registerWithMask("endereco.cep", ["#####-###"])}
            placeholder="Ex: 70254-010"
          />
          <FieldErrorMessage message={errors.endereco?.cep?.message} />
        </fieldset>
      </div>

      <div className="mt-4 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
        <Button type="button" variant="ghost" onClick={onCancel} disabled={isPending}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isPending} variant="primary">
          {isPending ? "Salvando..." : (isEditMode ? "Atualizar Aluno" : "Salvar Aluno")}
        </Button>
      </div>
    </form>
  );
}
