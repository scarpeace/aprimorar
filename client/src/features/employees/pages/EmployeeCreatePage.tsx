import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { Button, ButtonLink } from "@/components/ui/button";
import { FormField } from "@/components/ui/form-field";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import {
  employeeFormSchema,
  type EmployeeFormSchema,
} from "../forms/employeeFormSchema";
import { useEmployeeMutations } from "../hooks/emlpoyee-mutations";
import { FileUser } from "lucide-react";
import { employeeRequestDTODutyEnum } from "@/kubb";

export function EmployeeCreatePage() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<EmployeeFormSchema>({
    resolver: zodResolver(employeeFormSchema),
    defaultValues: {
      duty: "TEACHER",
    },
  });
  const registerWithMask = useHookFormMask(register);

  //TODO: Aqui eu vou mudar a syntaxe pra não ter que ficar me repetindo no nome.
  // vai ser createEmployee.isPending, createEmployee.mutate e por aí vai.
  const {
    createEmployee: { mutate: createEmployee, isPending: isSubmitting },
  } = useEmployeeMutations();
  const onSubmit = (data: EmployeeFormSchema) => {
    createEmployee({ data });
  };

  return (
    <div className={""}>
      <PageHeader
        title="Novo colaborador"
        description="Crie um novo cadastro de colaborador."
        backLink={"/employees"}
        Icon={FileUser}
      />

      <SectionCard
        title="Dados do colaborador"
        description="Preencha as informações abaixo para criar o cadastro."
      >
        <form
          className={""}
          onSubmit={handleSubmit(onSubmit)}
          autoComplete="off"
        >
          <div className={""}>
            <FormField
              className={""}
              label="Nome completo"
              htmlFor="name"
              error={errors.name?.message}
            >
              <input
                className="app-input"
                id="name"
                placeholder="Ex: Maria Silva"
                {...register("name")}
              />
            </FormField>

            <FormField
              className={""}
              label="Data de nascimento"
              htmlFor="birthdate"
              error={errors.birthdate?.message}
            >
              <input
                className="app-input"
                id="birthdate"
                type="date"
                {...register("birthdate")}
              />
            </FormField>

            <FormField
              className={""}
              label="Email"
              htmlFor="email"
              error={errors.email?.message}
            >
              <input
                className="app-input"
                id="email"
                type="email"
                placeholder="exemplo@dominio.com"
                {...register("email")}
              />
            </FormField>

            <FormField
              className={""}
              label="Contato"
              htmlFor="contact"
              error={errors.contact?.message}
            >
              <input
                className="app-input"
                id="contact"
                placeholder="(11) 99999-9999"
                {...registerWithMask("contact", [
                  "(99) 9999-9999",
                  "(99) 99999-9999",
                ])}
              />
            </FormField>

            <FormField
              className={""}
              label="CPF"
              htmlFor="cpf"
              error={errors.cpf?.message}
            >
              <input
                className="app-input"
                id="cpf"
                placeholder="000.000.000-00"
                {...registerWithMask("cpf", "999.999.999-99")}
              />
            </FormField>

            <FormField
              className={""}
              label="Chave PIX"
              htmlFor="pix"
              error={errors.pix?.message}
            >
              <input
                className="app-input"
                id="pix"
                placeholder="cpf/email/telefone/chave aleatória"
                {...register("pix")}
              />
            </FormField>

            <FormField
              className={""}
              label="Função"
              htmlFor="duty"
              error={errors.duty?.message}
            >
              <select id="duty" className="app-select" {...register("duty")}>
                <option value="TEACHER">{employeeRequestDTODutyEnum.TEACHER}</option>
                <option value="ADM">{employeeRequestDTODutyEnum.ADM}</option>
                <option value="THERAPIST">{employeeRequestDTODutyEnum.THERAPIST}</option>
                <option value="MENTOR">{employeeRequestDTODutyEnum.MENTOR}</option>
              </select>
            </FormField>
          </div>

          <div className={""}>
            <ButtonLink to="/employees" variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={isSubmitting} variant="primary">
              {isSubmitting ? "Salvando..." : "Criar colaborador"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </div>
  );
}
