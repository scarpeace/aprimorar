import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import type { StudentFormInput } from "@/features/students/schemas/studentFormSchema";
import type { FieldErrors, UseFormRegister } from "react-hook-form";
import type { useHookFormMask } from "use-mask-input";

type StudentInfoSectionProps = {
  register: UseFormRegister<StudentFormInput>;
  registerWithMask: ReturnType<typeof useHookFormMask>;
  errors: FieldErrors<StudentFormInput>;
  className?: string;
};

export function StudentInfoSection({
  register,
  registerWithMask,
  errors,
  className,
}: Readonly<StudentInfoSectionProps>) {
  return (
    <SectionCard
      title="Dados do aluno"
      description="Atualize os dados pessoais."
    >
      <div className={className}>
        <FormField
          label="Nome completo"
          htmlFor="name"
          error={errors.name?.message}
        >
          <input className="app-input" id="name" {...register("name")} />
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

        <FormField
          label="Email"
          htmlFor="email"
          error={errors.email?.message}
        >
          <input className="app-input" id="email" {...register("email")} />
        </FormField>

        <FormField
          label="Data de nascimento"
          htmlFor="birthdate"
          error={errors.birthdate?.message}
        >
          <input
            className="app-input"
            id="birthdate"
            {...registerWithMask("birthdate", "99/99/9999")}
          />
        </FormField>

        <FormField label="CPF" htmlFor="cpf" error={errors.cpf?.message}>
          <input
            className="app-input"
            id="cpf"
            {...registerWithMask("cpf", "999.999.999-99")}
          />
        </FormField>

        <FormField label="Escola" htmlFor="school" error={errors.school?.message}>
          <input
            className="app-input"
            id="school"
            {...register("school")}
          />
        </FormField>


      </div>
    </SectionCard>
  );
}
