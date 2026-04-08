import { Alert } from "@/components/ui/alert";
import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { PageHeader } from "@/components/ui/page-header";
import { useGetEmployeeById } from "@/kubb";
import { formatDateInputValue } from "@/lib/utils/formatter";
import { zodResolver } from "@hookform/resolvers/zod";
import { ChevronDown } from "lucide-react";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import { useHookFormMask } from "use-mask-input";
import { EmployeeForm } from "../forms/EmployeeForm";
import { EmployeeFormFields } from "../forms/EmployeeFormFields";
import {
  employeeFormSchema,
  type EmployeeFormSchema,
} from "../forms/employeeFormSchema";
import { useEmployeeMutations } from "../hooks/emlpoyee-mutations";

export function EmployeeEditPage() {
  const { id } = useParams<{ id: string }>();
  const employeeId = id ?? "";

  const {
    data: employeeData,
    isPending: isEmployeePending,
    error: employeeError,
  } = useGetEmployeeById(employeeId);

  const {
    updateEmployee: {
      mutate: updateEmployee,
      isPending: isUpdateEmployeePending,
      error: updateEmployeeError,
    },
  } = useEmployeeMutations();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<EmployeeFormSchema>({
    resolver: zodResolver(employeeFormSchema),
    values: {
      name: employeeData?.name ?? "",
      email: employeeData?.email ?? "",
      contact: employeeData?.contact ?? "",
      cpf: employeeData?.cpf ?? "",
      pix: employeeData?.pix ?? "",
      birthdate: employeeData?.birthdate
        ? formatDateInputValue(employeeData.birthdate)
        : "",
      duty: employeeData?.duty ?? "TEACHER",
    },
  });
  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: EmployeeFormSchema) => {
    updateEmployee({ employeeId, data });
  });

  return (
    <div className={""}>
      <PageHeader
        title="Editar colaborador"
        description="Atualize os dados do colaborador."
        backLink={""}
        Icon={ChevronDown}
      />

      {employeeError ? (
        <ErrorCard
          title="Erro ao carregar detalhes do colaborador"
          error={employeeError}
        />
      ) : isEmployeePending ? (
        <LoadingCard title="Carregando detalhes do colaborador" />
      ) : (
        <EmployeeForm onSubmit={onSubmit}>
          <EmployeeFormFields
            isUpdate={true}
            register={register}
            registerWithMask={registerWithMask}
            errors={errors}
          />

          {updateEmployeeError && (
            <Alert error={updateEmployeeError} variant="error" />
          )}

          <div className="flex flex-wrap justify-end gap-3">
            <Button
              type="submit"
              variant="success"
              disabled={isUpdateEmployeePending}
            >
              {isUpdateEmployeePending ? (
                <LoadingSpinner text={"Atualizando..."} />
              ) : (
                "Salvar alterações"
              )}
            </Button>

            <ButtonLink to={`/employees/${employeeId}`} variant="outline">
              Cancelar
            </ButtonLink>
          </div>
        </EmployeeForm>
      )}
    </div>
  );
}
