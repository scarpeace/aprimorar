import type {
  FieldErrors,
  UseFormRegister,
} from "react-hook-form";
import type { useHookFormMask } from "use-mask-input";
import type { AddressInputSchema } from "./addressSchema";
import { FieldsetInput } from "@/components/ui/fieldset-input";
import { StateSelectDropdown } from "./StateSelectDropdown";

type AddressDetailsFormProps = {
  register: UseFormRegister<any>;
  registerWithMask: ReturnType<typeof useHookFormMask>;
  prefix?: string;
  errors?: FieldErrors<AddressInputSchema>;
};

export function AddressFormFields({
  register,
  registerWithMask,
  prefix,
  errors,
}: Readonly<AddressDetailsFormProps>) {
  const withPrefix = (field: string) => (prefix ? `${prefix}.${field}` : field);

  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5 ">
      <FieldsetInput
        label="Rua"
        placeholder="Rua"
        type="text"
        registration={register(withPrefix("street"))}
        error={errors?.street?.message}
      />

      <FieldsetInput
        label="Número"
        placeholder="Número"
        type="text"
        registration={register(withPrefix("number"))}
        error={errors?.number?.message}
      />

      <FieldsetInput
        label="Complemento"
        placeholder="Complemento"
        type="text"
        registration={register(withPrefix("complement"))}
        error={errors?.complement?.message}
      />

      <FieldsetInput
        label="Bairro"
        placeholder="Bairro"
        type="text"
        registration={register(withPrefix("district"))}
        error={errors?.district?.message}
      />

      <FieldsetInput
        label="Cidade"
        placeholder="Cidade"
        type="text"
        registration={register(withPrefix("city"))}
        error={errors?.city?.message}
      />

      <div className="flex flex-row gap-3">
        <StateSelectDropdown
          registration={register(withPrefix("state"))}
          error={errors?.state?.message}
          label={"Estado"}
        />

        <FieldsetInput
          label="CEP"
          placeholder="CEP"
          type="text"
          registration={registerWithMask(withPrefix("zip"), "99999-999")}
          error={errors?.zip?.message}
        />
      </div>
    </div>
  );
}
