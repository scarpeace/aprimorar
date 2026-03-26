import { ButtonLink } from "@/components/ui/button";
import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import { ParentSelectDropdown } from "@/features/parents/components/ParentSelectDropdown";
import type { StudentFormInput } from "@/features/students/schemas/studentFormSchema";
import type { FieldErrors, UseFormRegister, UseFormSetValue } from "react-hook-form";

type StudentResponsibleSectionProps = {
  selectedParentId: string;
  register: UseFormRegister<StudentFormInput>;
  setValue: UseFormSetValue<StudentFormInput>;
  errors: FieldErrors<StudentFormInput>;
  className?: string;
  fieldClassName?: string;
  span2ClassName?: string;
};

export function ParentDetailsForm({
  selectedParentId,
  register,
  setValue,
  errors,
  className,
  fieldClassName,
  span2ClassName,
}: Readonly<StudentResponsibleSectionProps>) {
  return (
    <SectionCard
      title="Responsável"
      description="Selecione um responsável já cadastrado no sistema."
    >
      <div className={className}>
        <FormField
          className={`${fieldClassName ?? ""} ${span2ClassName ?? ""}`.trim()}
          label="Responsável"
          htmlFor="parentId"
          error={errors.parentId?.message}
        >
          <input type="hidden" {...register("parentId")} />

          <ParentSelectDropdown
            value={selectedParentId}
            onChange={(parentId) => {
              setValue("parentId", parentId, {
                shouldValidate: true,
                shouldDirty: true,
              });
            }}
            hasError={!!errors.parentId}
          />

          <p className="mt-1 text-xs text-base-content/70">
            Não encontrou o responsável?{" "}
            <ButtonLink
              to="/parents/new"
              variant="ghost"
              size="sm"
              className="h-auto p-0 underline"
            >
              Cadastre um novo aqui
            </ButtonLink>
          </p>
        </FormField>
      </div>
    </SectionCard>
  );
}
