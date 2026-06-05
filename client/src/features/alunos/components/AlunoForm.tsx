import { zodResolver } from "@hookform/resolvers/zod";
import { TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";

import { Button } from "@/components/ui/button";
import { ResponsavelSelectDropdown } from "@/features/responsaveis/components/ResponsavelSelectDropdown";
import { brazilianStates } from "@/lib/utils/brazilianStates";
import type { AlunoResponseDTO } from "@/kubb";
import { formatDateForInput } from "@/lib/utils/formatter";
import { alunoFormSchema, type AlunoFormSchema } from "../lib/alunoFormSchema.ts";
import { useAlunoMutations } from "../hooks/use-aluno-mutations";

interface AlunoFormProps {
  initialData?: AlunoResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
}

export function AlunoForm({ initialData, onSuccess, onCancel }: AlunoFormProps) {
  const { createStudent, updateStudent } = useAlunoMutations();
  const isEditMode = !!initialData;

  const { register, control, handleSubmit, formState: { errors } } = useForm<AlunoFormSchema>({
    resolver: zodResolver(alunoFormSchema),
    mode: "onBlur",
    defaultValues: {
      name: initialData?.name,
      cpf: initialData?.cpf,
      birthdate: formatDateForInput(initialData?.birthdate),
      contact: initialData?.contact,
      email: initialData?.email,
      school: initialData?.school,
      parentId: initialData?.parentId,
      address: {
        street: initialData?.address.street,
        complement: initialData?.address.complement ?? "N/A",
        district: initialData?.address.district,
        city: initialData?.address.city,
        state: initialData?.address.state ?? brazilianStates.DF,
        zip: initialData?.address.zip,
      },
    },
  });

  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: AlunoFormSchema) => {
    console.log(data)
    if (isEditMode && initialData.id) {
      updateStudent.mutate(
        { studentId: initialData.id, data },
        { onSuccess }
      );
    } else {
      createStudent.mutate(
        { data },
        { onSuccess }
      );
    }
  });

  const isPending = createStudent.isPending || updateStudent.isPending;

  return (
    <form className="flex flex-col" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 md:grid-cols-3">
        <ResponsavelSelectDropdown className="col-span-3" label="Responsável" control={control} error={errors.parentId?.message} />

        <fieldset className="fieldset md:col-span-2 mr-3">
          <legend className="fieldset-legend">Nome</legend>
          <input type="text" className="input w-full" {...register("name")} placeholder="Nome Completo" />
          {errors?.name && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.name.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Data de Nascimento</legend>
          <input type="text" className="input w-full" {...registerWithMask("birthdate", ["##/##/####"])} placeholder="Ex: 01/01/1990" />
          {errors?.birthdate && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.birthdate.message}
            </p>
          )}
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
          {errors?.cpf && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.cpf.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset mr-3">
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
          <legend className="fieldset-legend">Email</legend>
          <input type="text" className="input w-full" {...register("email")} placeholder="email@email.com" />
          {errors?.email && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.email.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset md:col-span-3">
          <legend className="fieldset-legend">Escola</legend>
          <input type="text" className="input w-full" {...register("school")} placeholder="Ex: Leonardo da Vinci" />
          {errors?.school && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.school.message}
            </p>
          )}
        </fieldset>

        <div className="divider col-span-3 m-0" />

        <fieldset className="fieldset md:col-span-2 mr-3">
          <legend className="fieldset-legend">Rua</legend>
          <input type="text" className="input w-full" {...register("address.street")} placeholder="Ex: SQS 406, Bloco C" />
          {errors?.address?.street && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.address.street.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Bairro</legend>
          <input type="text" className="input w-full" {...register("address.district")} placeholder="Ex: Asa sul" />
          {errors?.address?.district && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.address.district.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset mr-3">
          <legend className="fieldset-legend">Complemento</legend>
          <input type="text" className="input w-full" {...register("address.complement")} placeholder="Ex: Apto 101" />
          {errors?.address?.complement && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.address.complement.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset mr-3">
          <legend className="fieldset-legend">Cidade</legend>
          <input type="text" className="input w-full" {...register("address.city")} placeholder="Ex: Brasília" />
          {errors?.address?.city && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.address.city.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Estado</legend>
          <select className="select select-bordered w-full" {...register("address.state")}>
            {Object.values(brazilianStates).map((content) => (
              <option key={content} value={content}>
                {content}
              </option>
            ))}
          </select>
          {errors?.address?.state && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.address.state.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">CEP</legend>
          <input
            type="text"
            className="input w-full"
            {...registerWithMask("address.zip", ["#####-###"])}
            placeholder="Ex: 70254-010"
          />
          {errors?.address?.zip && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.address.zip.message}
            </p>
          )}
        </fieldset>
      </div>

      <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
        <Button type="button" variant="ghost" onClick={onCancel}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isPending} variant="primary">
          {isPending ? "Salvando..." : (isEditMode ? "Atualizar Aluno" : "Salvar Aluno")}
        </Button>
      </div>
    </form>
  );
}
