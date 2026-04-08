import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";

import type { FieldErrors, UseFormRegister } from "react-hook-form";
import type { useHookFormMask } from "use-mask-input";
import type { EmployeeFormSchema } from "./employeeFormSchema";
import { employeeRequestDTODutyEnum } from "@/kubb";

type EmployeeFormFieldsProps = {
  register: UseFormRegister<EmployeeFormSchema>;
  registerWithMask: ReturnType<typeof useHookFormMask>;
  errors: FieldErrors<EmployeeFormSchema>;
  className?: string;
  isUpdate?: boolean;
};

export function EmployeeFormFields({
  register,
  registerWithMask,
  errors,
  className,
  isUpdate,
}: Readonly<EmployeeFormFieldsProps>) {
  return (
    <SectionCard title="Dados do aluno" description={""}>
      <div className={`grid grid-cols-2 gap-4 ${className}`}>
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
            disabled={isUpdate}
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
            <option value="TEACHER">
              {employeeRequestDTODutyEnum.TEACHER}
            </option>
            <option value="ADM">{employeeRequestDTODutyEnum.ADM}</option>
            <option value="THERAPIST">
              {employeeRequestDTODutyEnum.THERAPIST}
            </option>
            <option value="MENTOR">{employeeRequestDTODutyEnum.MENTOR}</option>
          </select>
        </FormField>
      </div>
    </SectionCard>
  );
}
