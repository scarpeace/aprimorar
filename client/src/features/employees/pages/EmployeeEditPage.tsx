import { Alert } from "@/components/ui/alert";
import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { PageLayout } from "@/components/layout/PageLayout";
import { SectionCard } from "@/components/ui/section-card";
import { useGetEmployeeById } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { formatDateInputValue } from "@/lib/utils/formatter";
import { zodResolver } from "@hookform/resolvers/zod";
import { FileUser, TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import { useHookFormMask } from "use-mask-input";
import {
  employeeFormSchema,
  type EmployeeFormSchema,
} from "../forms/employeeFormSchema";
import { employeeRequestDTODutyEnum } from "@/kubb";
import { useEmployeeMutations } from "../hooks/emlpoyee-mutations";

export function EmployeeEditPage() {
  const { id } = useParams<{ id: string }>();
  const employeeId = id ?? "";

  const employeeQuery = useGetEmployeeById(employeeId);
  const { updateEmployee } = useEmployeeMutations();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<EmployeeFormSchema>({
    resolver: zodResolver(employeeFormSchema),
    values: {
      name: employeeQuery.data?.name ?? "",
      email: employeeQuery.data?.email ?? "",
      contact: employeeQuery.data?.contact ?? "",
      cpf: employeeQuery.data?.cpf ?? "",
      pix: employeeQuery.data?.pix ?? "",
      birthdate: employeeQuery.data?.birthdate
        ? formatDateInputValue(employeeQuery.data.birthdate)
        : "",
      duty: employeeQuery.data?.duty ?? "TEACHER",
    },
  });
  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: EmployeeFormSchema) => {
    updateEmployee.mutate({ employeeId, data });
  });

  const headerProps = {
    title: "Editar colaborador",
    description: "Atualize os dados do colaborador.",
    backLink: `/employees/${employeeId}`,
    Icon: FileUser,
  };

  if (employeeQuery.error) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar detalhes do colaborador"
          error={employeeQuery.error}
        />
      </PageLayout>
    );
  }

  if (employeeQuery.isPending || !employeeQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando detalhes do colaborador" />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <SectionCard
        title="Editar colaborador"
        description="Atualize os dados do colaborador."
      >
        {updateEmployee.isError && (
          <Alert
            error={getFriendlyErrorMessage(updateEmployee.error)}
            variant="error"
          />
        )}

        <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
          <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">
            <fieldset className="fieldset">
              <legend className="fieldset-legend">Nome</legend>
              <input type="text" className="input" {...register("name")} placeholder="Nome Completo"/>
              {errors?.name && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.name.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Data de Nascimento</legend>
              <input type="text" className="input" {...registerWithMask("birthdate", ["##/##/####"])} placeholder="dd/mm/yyyy"/>
              {errors?.birthdate && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.birthdate.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Email</legend>
              <input type="text" className="input" {...register("email")} placeholder="email@email.com"/>
              {errors?.email && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.email.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Contato</legend>
              <input type="text" className="input" placeholder="Ex: (61) 99633-2332" {...registerWithMask("contact", [
                "(##) #####-####",
                "(##) ####-####",
              ])}/>
              {errors?.contact && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.contact.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">CPF</legend>
              <input type="text" className="input" disabled={true} placeholder="Ex: 123.456.789-00" {...registerWithMask("cpf", ["###.###.###-##"])} />
              {errors?.cpf && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.cpf.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Chave PIX</legend>
              <input type="text" className="input" {...register("pix")} placeholder="cpf/email/telefone/chave aleatória"/>
              {errors?.pix && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.pix.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Função</legend>
              <select
                className="select select-bordered w-full"
                {...register("duty")}
              >
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

          <div className="mt-1 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
            <ButtonLink to={`/employees/${employeeId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" variant="primary" disabled={updateEmployee.isPending}>
              {updateEmployee.isPending ? "Salvando..." : "Salvar"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </PageLayout>
  );
}
