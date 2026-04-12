import { Alert } from "@/components/ui/alert";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";

import { Button, ButtonLink } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { SectionCard } from "@/components/ui/section-card";
import { employeeRequestDTODutyEnum } from "@/kubb";
import {
  employeeFormSchema,
  type EmployeeFormSchema,
} from "../forms/employeeFormSchema";
import { useEmployeeMutations } from "../hooks/emlpoyee-mutations";
import { FileUser, TriangleAlert } from "lucide-react";

export function EmployeeCreatePage() {
  const { createEmployee } = useEmployeeMutations();

  const { register, handleSubmit, formState: { errors } } = useForm<EmployeeFormSchema>({
    resolver: zodResolver(employeeFormSchema),
    defaultValues: {
      duty: "TEACHER",
    },
  });
  const registerWithMask = useHookFormMask(register);


  const onSubmit = (data: EmployeeFormSchema) => {
    createEmployee.mutate({ data });
  };

  const headerProps = {
    title: "Novo colaborador",
    description: "Crie um novo cadastro de colaborador.",
    backLink: "/employees",
    Icon: FileUser,
  };

  return (
    <PageLayout {...headerProps}>

      <SectionCard
        title="Dados do colaborador"
        description="Preencha as informações abaixo para criar o cadastro."
      >
        {createEmployee.isError && (
          <Alert error={createEmployee.error} variant="error" />
        )}

        <form className="flex flex-col gap-3" onSubmit={handleSubmit(onSubmit)} autoComplete="off">
          <div className="grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-5">
            <fieldset className="fieldset">
              <legend className="fieldset-legend">Nome</legend>
              <input
                type="text"
                className="input"
                {...register("name")}
                placeholder="Nome Completo"
              />
              {errors?.name && (
                <p className="label text-error">
                  <TriangleAlert className="w-3 h-3" />
                  {errors.name.message}
                </p>
              )}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Data de Nascimento</legend>
              <input
                type="date"
                className="input"
                {...register("birthdate")}
              />
              {errors?.birthdate && (
                <p className="label text-error">
                  <TriangleAlert className="w-3 h-3" />
                  {errors.birthdate.message}
                </p>
              )}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Email</legend>
              <input
                type="text"
                className="input"
                {...register("email")}
                placeholder="email@email.com"
              />
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
                className="input"
                placeholder="Ex: (61) 99633-2332"
                {...registerWithMask("contact", [
                  "(##) #####-####",
                  "(##) ####-####",
                ])}
              />
              {errors?.contact && (
                <p className="label text-error">
                  <TriangleAlert className="w-3 h-3" />
                  {errors.contact.message}
                </p>
              )}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">CPF</legend>
              <input
                type="text"
                className="input"
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

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Chave PIX</legend>
              <input
                type="text"
                className="input"
                {...register("pix")}
                placeholder="cpf/email/telefone/chave aleatória"
              />
              {errors?.pix && (
                <p className="label text-error">
                  <TriangleAlert className="w-3 h-3" />
                  {errors.pix.message}
                </p>
              )}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Função</legend>
              <select
                className="select select-bordered w-full"
                {...register("duty")}
              >
                <option value="TEACHER">{employeeRequestDTODutyEnum.TEACHER}</option>
                <option value="ADM">{employeeRequestDTODutyEnum.ADM}</option>
                <option value="THERAPIST">{employeeRequestDTODutyEnum.THERAPIST}</option>
                <option value="MENTOR">{employeeRequestDTODutyEnum.MENTOR}</option>
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
            <ButtonLink to="/employees" variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={createEmployee.isPending} variant="primary">
              {createEmployee.isPending ? "Salvando..." : "Salvar"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </PageLayout>
  );
}
