import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import type { FieldErrors, UseFormRegister } from "react-hook-form";
import { addressRequestDTOStateEnum } from "@/kubb";

type AddressInfoSectionProps = {
  register: UseFormRegister<any>;
  errors: FieldErrors<any>;
  className?: string;
};

export function AddressDetailsForm({
  register,
  errors,
  className,
}: Readonly<AddressInfoSectionProps>) {


  return (
    <SectionCard
      title="Endereço"
      description="Atualize os dados de endereço do aluno."
    >
      <div className={className}>
        <FormField
          className=""
          label="Rua"
          htmlFor={`address.street`}
          error={errors[`address.street`]?.message as string}
        >
          <input
            className="app-input"
            id="street"
            placeholder="Ex: Rua das Flores"
            {...register(`address.street`)}
          />
        </FormField>

        <FormField
          className={"col-span-1"}
          label="Número"
          htmlFor={`address.number`}
          error={errors[`address.number`]?.message as string}
        >
          <input
            className="app-input"
            id="number"
            placeholder="Ex: 123"
            {...register(`address.number`)}
          />
        </FormField>

        <FormField
          className=""
          label="Complemento"
          htmlFor={`address.complement`}
          error={errors[`address.complement`]?.message as string}
        >
          <input
            className="app-input"
            id="complement"
            placeholder="Apto, bloco, etc"
            {...register(`address.complement`)}
          />
        </FormField>

        <FormField
          className=""
          label="Bairro"
          htmlFor={`address.district`}
          error={errors[`address.district`]?.message as string}
        >
          <input
            className="app-input"
            id="district"
            placeholder="Ex: Centro"
            {...register(`address.district`)}
          />
        </FormField>

        <FormField
          className=""
          label="Cidade"
          htmlFor={`address.city`}
          error={errors[`address.city`]?.message as string}
        >
          <input
            className="app-input"
            id="city"
            placeholder="Ex: Brasília"
            {...register(`address.city`)}
          />
        </FormField>

        <FormField
          className="shrink"
          label="Estado"
          htmlFor={`address.state`}
          error={errors[`address.state`]?.message as string}
        >
          <select
            className="app-select"
            id="state"
            {...register(`address.state`)}
          >
            {Object.values(addressRequestDTOStateEnum).map((state) => (
              <option key={state} value={state}>
                {state}
              </option>
            ))}
          </select>
        </FormField>

        <FormField
          className=""
          label="CEP"
          htmlFor={`address.zip`}
          error={errors[`address.zip`]?.message as string}
        >
          <input
            className="app-input"
            id="zip"
            placeholder="00000-000"
            {...register(`address.zip`)}
          />
        </FormField>
      </div>
    </SectionCard>
  );
}
