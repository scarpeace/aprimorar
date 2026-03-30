import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import type { FieldErrors, UseFormRegister } from "react-hook-form";
import {
  addressRequestDTOStateEnum,
} from "@/kubb";
import type { AddressInputSchema } from "./hooks/addressSchema";
import type { useHookFormMask } from "use-mask-input";

type AddressDetailsFormProps = {
  register: UseFormRegister<any>;
  registerWithMask: ReturnType<typeof useHookFormMask>;
  prefix?: string;
  errors?: FieldErrors<AddressInputSchema>;
  className?: string;
};

export function AddressDetailsForm({
  register,
  registerWithMask,
  prefix,
  errors,
  className,
}: Readonly<AddressDetailsFormProps>) {
  const withPrefix = (field: string) => (prefix ? `${prefix}.${field}` : field);

  return (
    <SectionCard
      title="Endereço"
      description="Atualize os dados de endereço do aluno."
    >
      <div className={className}>
        <FormField
          className=""
          label="Rua"
          htmlFor={withPrefix(`street`)}
          error={errors?.street?.message}
        >
          <input
            className="app-input"
            id="street"
            placeholder="Ex: Rua das Flores"
            {...register(`${withPrefix(`street`)}`)}
          />
        </FormField>

        <FormField
          className={"col-span-1"}
          label="Número"
          htmlFor={withPrefix(`number`)}
          error={errors?.number?.message}
        >
          <input
            className="app-input"
            id="number"
            placeholder="Ex: 123"
            {...register(`${withPrefix(`number`)}`)}
          />
        </FormField>

        <FormField
          className=""
          label="Complemento"
          htmlFor={withPrefix(`complement`)}
        >
          <input
            className="app-input"
            id="complement"
            placeholder="Apto, bloco, etc"
            {...register(`${withPrefix(`complement`)}`)}
          />
        </FormField>

        <FormField
          className=""
          label="Bairro"
          htmlFor={withPrefix(`district`)}
          error={errors?.district?.message}
        >
          <input
            className="app-input"
            id="district"
            placeholder="Ex: Centro"
            {...register(`${withPrefix(`district`)}`)}
          />
        </FormField>

        <FormField
          className=""
          label="Cidade"
          htmlFor={withPrefix(`city`)}
          error={errors?.city?.message}
        >
          <input
            className="app-input"
            id="city"
            placeholder="Ex: Brasília"
            {...register(`${withPrefix(`city`)}`)}
          />
        </FormField>

        <FormField
          className="shrink"
          label="Estado"
          htmlFor={withPrefix(`state`)}
          error={errors?.state?.message}
        >
          <select
            className="app-select"
            id="state"
            {...register(`${withPrefix(`state`)}`)}
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
          htmlFor={withPrefix(`zip`)}
          error={errors?.zip?.message}
        >
          <input
            className="app-input"
            id="zip"
            placeholder="00000-000"
             {...registerWithMask(withPrefix(`zip`), "99999-999")}
          />
        </FormField>
      </div>
    </SectionCard>
  );
}
