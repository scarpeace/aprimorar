import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";

import type { Control, FieldErrors, UseFormRegister } from "react-hook-form";
import type { useHookFormMask } from "use-mask-input";
import type { StudentFormSchema } from "./studentFormSchema";
import { ParentSelectDropdown } from "@/features/parents/components/ParentSelectDropdown";


type StudentFormFieldsProps = {
  register: UseFormRegister<StudentFormSchema>;
  registerWithMask: ReturnType<typeof useHookFormMask>;
  errors: FieldErrors<StudentFormSchema>;
  control: Control<StudentFormSchema>;
  className?: string;
  isUpdate?: boolean;
};

export function StudentFormFields({
  register,
  registerWithMask,
  errors,
  control,
  className,
  isUpdate,
}: Readonly<StudentFormFieldsProps>) {
  return (
    <SectionCard
      title="Dados do aluno"
      description={`Não encontrou um responsável cadastrado? ${<a href='/parents/new'>Cadastre-o aqui!</a>}`}
    >
      <div className={`grid grid-cols-2 gap-4 ${className}`}>
        <a href='/parents/new'>Cadastre-o aqui!</a>
        <ParentSelectDropdown className="col-span-3" control={control} error={errors.parentId?.message} />

        <FormField
          label="Nome completo"
          htmlFor="name"
          error={errors.name?.message}
          className="col-span-2"
        >
          <input className="app-input" id="name" {...register("name")} />
        </FormField>

           <FormField label="CPF" htmlFor="cpf" error={errors.cpf?.message}>
          <input
            className="app-input"
            disabled={isUpdate}
            id="cpf"
            {...registerWithMask("cpf", "999.999.999-99")}
          />
        </FormField>

        <FormField
          label="Contato"
          htmlFor="contact"
          error={errors.contact?.message}
        >
          <input
            className="app-input"
            id="contact"
            {...registerWithMask("contact", [
              "(99) 9999-9999",
              "(99) 99999-9999",
            ])}
          />
        </FormField>

        <FormField label="Email" htmlFor="email" error={errors.email?.message}>
          <input className="app-input" id="email" {...register("email")} />
        </FormField>

        <FormField
          label="Data de nascimento"
          htmlFor="birthdate"
          error={errors.birthdate?.message}
        >
          <input
            type="date"
            className="app-input"
            id="birthdate"
            {...register("birthdate")}
          />
        </FormField>

        <FormField
          label="Escola"
          htmlFor="school"
          error={errors.school?.message}
        >
          <input className="app-input" id="school" {...register("school")} />
        </FormField>
      </div>
    </SectionCard>
  );
}
