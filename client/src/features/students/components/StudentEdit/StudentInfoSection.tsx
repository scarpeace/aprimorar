import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import type { UpdateStudentMutationRequest } from "@/kubb/types";
import type { FieldErrors, UseFormRegister } from "react-hook-form";
import type { useHookFormMask } from "use-mask-input";

type StudentInfoSectionProps = {
  register: UseFormRegister<UpdateStudentMutationRequest>;
  registerWithMask: ReturnType<typeof useHookFormMask>;
  errors: FieldErrors<UpdateStudentMutationRequest>;
};

export function StudentInfoSection({
  register,
  registerWithMask,
  errors,
}: Readonly<StudentInfoSectionProps>) {
  return (
    <SectionCard
      title="Dados do aluno"
      description="Atualize os dados pessoais."
    >
      <div className={""}>
        <FormField label="Nome completo" htmlFor="name" error={errors.name?.message}>
          <input className="app-input" id="name" {...register("name")} />
        </FormField>

        <FormField
          label="CPF"
          htmlFor="cpf"
          error={errors.cpf?.message}
        >
          <input
            className="app-input"
            id="cpf"
            {...registerWithMask("cpf", "999.999.999-99")}
          />
        </FormField>
      </div>
    </SectionCard>
  );
}
