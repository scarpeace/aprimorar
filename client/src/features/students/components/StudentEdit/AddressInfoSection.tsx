import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import type { FieldErrors, UseFormRegister } from "react-hook-form";
import type { UpdateStudentMutationRequest } from "@/kubb";

type AddressInfoSectionProps = {
  register: UseFormRegister<UpdateStudentMutationRequest>;
  errors: FieldErrors<UpdateStudentMutationRequest>;
  className?: string;
  fieldClassName?: string;
  span2ClassName?: string;
};

export function AddressInfoSection({
  register,
  errors,
  className,
  fieldClassName,
  span2ClassName,
}: Readonly<AddressInfoSectionProps>) {
  return (
    <SectionCard
      title="Endereço"
      description="Atualize os dados de endereço do aluno."
    >
      <div className={className}>
        <FormField
          className={`${fieldClassName ?? ""} ${span2ClassName ?? ""}`.trim()}
          label="Rua"
          htmlFor="address.street"
          error={errors.address?.street?.message}
        >
          <input
            className="app-input"
            id="address.street"
            placeholder="Ex: Rua das Flores"
            {...register("address.street")}
          />
        </FormField>

        <FormField
          className={fieldClassName}
          label="Número"
          htmlFor="address.number"
          error={errors.address?.number?.message}
        >
          <input
            className="app-input"
            id="address.number"
            placeholder="Ex: 123"
            {...register("address.number")}
          />
        </FormField>

        <FormField
          className={fieldClassName}
          label="Complemento"
          htmlFor="address.complement"
          error={errors.address?.complement?.message}
        >
          <input
            className="app-input"
            id="address.complement"
            placeholder="Apto, bloco, etc"
            {...register("address.complement")}
          />
        </FormField>

        <FormField
          className={fieldClassName}
          label="Bairro"
          htmlFor="address.district"
          error={errors.address?.district?.message}
        >
          <input
            className="app-input"
            id="address.district"
            placeholder="Ex: Centro"
            {...register("address.district")}
          />
        </FormField>

        <FormField
          className={fieldClassName}
          label="Cidade"
          htmlFor="address.city"
          error={errors.address?.city?.message}
        >
          <input
            className="app-input"
            id="address.city"
            placeholder="Ex: Brasília"
            {...register("address.city")}
          />
        </FormField>

        <FormField
          className={fieldClassName}
          label="Estado"
          htmlFor="address.state"
          error={errors.address?.state?.message}
        >
          <input
            className="app-input"
            id="address.state"
            {...register("address.state")}
          />
        </FormField>

        <FormField
          className={fieldClassName}
          label="CEP"
          htmlFor="address.zip"
          error={errors.address?.zip?.message}
        >
          <input
            className="app-input"
            id="address.zip"
            placeholder="00000-000"
            {...register("address.zip")}
          />
        </FormField>
      </div>
    </SectionCard>
  );
}
