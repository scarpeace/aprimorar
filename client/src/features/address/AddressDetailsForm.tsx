import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import type { FieldErrors, UseFormRegister } from "react-hook-form";
import { addressRequestDTOStateEnum } from "@/kubb";
import type { AddressFormInput } from "./schemas/addressFormSchema";

type AddressInfoSectionProps = {
  address: AddressFormInput;
  register: UseFormRegister<AddressFormInput>;
  errors: FieldErrors<AddressFormInput>;
  className?: string;
};

export function AddressDetailsForm({
  address,
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
          htmlFor="street"
          error={errors.street?.message}
        >
          <input
            className="app-input"
            id="street"
            placeholder="Ex: Rua das Flores"
            {...register("street")}
          />
        </FormField>

        <FormField
          className={"col-span-1"}
          label="Número"
          htmlFor="number"
          error={errors?.number?.message}
        >
          <input
            className="app-input"
            id="number"
            placeholder="Ex: 123"
            {...register("number")}
          />
        </FormField>

        <FormField
          className=""
          label="Complemento"
          htmlFor="complement"
          error={errors?.complement?.message}
        >
          <input
            className="app-input"
            id="complement"
            placeholder="Apto, bloco, etc"
            {...register("complement")}
          />
        </FormField>

        <FormField
          className=""
          label="Bairro"
          htmlFor="district"
          error={errors?.district?.message}
        >
          <input
            className="app-input"
            id="district"
            placeholder="Ex: Centro"
            {...register("district")}
          />
        </FormField>

        <FormField
          className=""
          label="Cidade"
          htmlFor="city"
          error={errors?.city?.message}
        >
          <input
            className="app-input"
            id="city"
            placeholder="Ex: Brasília"
            {...register("city")}
          />
        </FormField>

        <FormField
          className=""
          label="Estado"
          htmlFor="state"
          error={errors?.state?.message}
        >
          <select
            className="app-select"
            id="state"
            {...register("state")}
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
          htmlFor="zip"
          error={errors?.zip?.message}
        >
          <input
            className="app-input"
            id="zip"
            placeholder="00000-000"
            {...register("zip")}
          />
        </FormField>
      </div>
    </SectionCard>
  );
}
