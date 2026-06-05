import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useHookFormMask } from "use-mask-input";
import { Button } from "@/components/ui/button";
import { TriangleAlert } from "lucide-react";
import { colaboradorRequestDTODutyEnum } from "@/kubb";
import type { ColaboradorResponseDTO } from "@/kubb";
import { brazilianStates } from "@/lib/utils/brazilianStates";
import { colaboradorFormSchema, type ColaboradorFormSchema } from "../lib/employeeFormSchema.ts";
import { useColaboradorMutations } from "../hooks/use-colaborador-mutations";
import { formatDateForInput } from "@/lib/utils/formatter";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

interface ColaboradorFormProps {
  initialData?: ColaboradorResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
}

export function ColaboradorForm({ initialData, onSuccess, onCancel }: ColaboradorFormProps) {
  const { createEmployee, updateEmployee } = useColaboradorMutations();
  const isEditMode = !!initialData;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ColaboradorFormSchema>({
    resolver: zodResolver(colaboradorFormSchema),
    defaultValues: {
      name: initialData?.name ?? "",
      email: initialData?.email ?? "",
      contact: initialData?.contact ?? "",
      cpf: initialData?.cpf ?? "",
      pix: initialData?.pix ?? "",
      birthdate: formatDateForInput(initialData?.birthdate),
      duty: initialData?.duty ?? "TEACHER",
      address: {
        street: initialData?.address?.street ?? "",
        complement: initialData?.address?.complement ?? "N/A",
        district: initialData?.address?.district ?? "",
        city: initialData?.address?.city ?? "",
        state: initialData?.address?.state ?? "DF",
        zip: initialData?.address?.zip ?? "",
      },
    },
    mode: "onBlur",
  });

  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: ColaboradorFormSchema) => {
    if (isEditMode && initialData.id) {
      updateEmployee.mutate(
        { colaboradorId: initialData.id, data },
        {
          onSuccess: () => onSuccess(),
          onError: (error) => toast.error(getFriendlyErrorMessage(error)),
        }
      );
    } else {
      createEmployee.mutate(
        { data },
        {
          onSuccess: () => onSuccess(),
          onError: (error) => toast.error(getFriendlyErrorMessage(error)),
        }
      );
    }
  });

  const isPending = createEmployee.isPending || updateEmployee.isPending;

  return (
    <form className="flex flex-col" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 md:grid-cols-3">
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

        <fieldset className="fieldset mr-3">
          <legend className="fieldset-legend">Chave PIX</legend>
          <input type="text" className="input w-full" {...register("pix")} placeholder="cpf/email/telefone/chave aleatória" />
          {errors?.pix && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.pix.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Função</legend>
          <select className="select select-bordered w-full" {...register("duty")}>
            <option value={colaboradorRequestDTODutyEnum.TEACHER}>PROFESSOR</option>
            <option value={colaboradorRequestDTODutyEnum.ADM}>ADM</option>
            <option value={colaboradorRequestDTODutyEnum.THERAPIST}>TERAPEUTA</option>
            <option value={colaboradorRequestDTODutyEnum.MENTOR}>MENTOR</option>
          </select>
          {errors?.duty && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.duty.message}
            </p>
          )}
        </fieldset>

        <div className="divider col-span-3" />

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
          <input type="text" className="input w-full" {...register("address.district")} placeholder="Ex: Asa Sul" />
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
          {isPending ? "Salvando..." : (isEditMode ? "Atualizar Colaborador" : "Salvar Colaborador")}
        </Button>
      </div>
    </form>
  );
}
