import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useHookFormMask } from "use-mask-input";
import { Button } from "@/components/ui/button";
import { TriangleAlert } from "lucide-react";
import { employeeRequestDTODutyEnum } from "@/kubb";
import type { EmployeeResponseDTO } from "@/kubb";
import { employeeFormSchema, type EmployeeFormSchema } from "../forms/employeeFormSchema";
import { useEmployeeMutations } from "../hooks/emlpoyee-mutations";
import { formatDateInputValue } from "@/lib/utils/formatter";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";

interface EmployeeFormProps {
  initialData?: EmployeeResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
}

export function EmployeeForm({ initialData, onSuccess, onCancel }: EmployeeFormProps) {
  const { createEmployee, updateEmployee } = useEmployeeMutations();
  const isEditMode = !!initialData;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<EmployeeFormSchema>({
    resolver: zodResolver(employeeFormSchema),
    defaultValues: {
      name: initialData?.name ?? "",
      email: initialData?.email ?? "",
      contact: initialData?.contact ?? "",
      cpf: initialData?.cpf ?? "",
      pix: initialData?.pix ?? "",
      birthdate: initialData?.birthdate
        ? formatDateInputValue(initialData.birthdate)
        : "",
      duty: initialData?.duty ?? "TEACHER",
    },
    mode: "onBlur",
  });
  
  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: EmployeeFormSchema) => {
    if (isEditMode && initialData.id) {
      updateEmployee.mutate(
        { employeeId: initialData.id, data },
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
    <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-5">
        <fieldset className="fieldset md:col-span-2">
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
          <input type="text" className="input w-full" {...registerWithMask("birthdate", ["##/##/####"])} placeholder="dd/mm/yyyy" />
          {errors?.birthdate && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.birthdate.message}
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

        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Email</legend>
          <input type="text" className="input w-full" {...register("email")} placeholder="email@email.com" />
          {errors?.email && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.email.message}
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

        <fieldset className="fieldset">
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
            <option value={employeeRequestDTODutyEnum.TEACHER}>PROFESSOR</option>
            <option value={employeeRequestDTODutyEnum.ADM}>ADM</option>
            <option value={employeeRequestDTODutyEnum.THERAPIST}>TERAPEUTA</option>
            <option value={employeeRequestDTODutyEnum.MENTOR}>MENTOR</option>
          </select>
          {errors?.duty && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.duty.message}
            </p>
          )}
        </fieldset>
      </div>

      <div className="mt-4 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
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
