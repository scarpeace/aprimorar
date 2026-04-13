import { TriangleAlert } from "lucide-react";
import type {
  FieldErrors,
  FieldValues,
  UseFormRegister,
} from "react-hook-form";
import type { useHookFormMask } from "use-mask-input";

import type { AddressInputSchema } from "./addressSchema";
import { StateSelectDropdown } from "./StateSelectDropdown";

type AddressDetailsFormProps = {
  register: UseFormRegister<FieldValues>;
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
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">
      <fieldset className="fieldset">
        <legend className="fieldset-legend">Rua</legend>
        <input type="text" className="input" placeholder="Rua" {...register(withPrefix("street"))} />
        {errors?.street && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.street.message}</p>)}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Complemento</legend>
        <input type="text" className="input" placeholder="Complemento" {...register(withPrefix("complement"))} />
        {errors?.complement && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.complement.message}</p>)}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Bairro</legend>
        <input type="text" className="input" placeholder="Bairro" {...register(withPrefix("district"))} />
        {errors?.district && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.district.message}</p>)}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Cidade</legend>
        <input type="text" className="input" placeholder="Cidade" {...register(withPrefix("city"))} />
        {errors?.city && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.city.message}</p>)}
      </fieldset>

      <div className="flex flex-row gap-3">
        <StateSelectDropdown
          registration={register(withPrefix("state"))}
          error={errors?.state?.message}
          label={"Estado"}
        />

        <fieldset className="fieldset">
          <legend className="fieldset-legend">CEP</legend>
          <input type="text" className="input" placeholder="CEP" {...registerWithMask(withPrefix("zip"), "99999-999")}/>
          {errors?.zip && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.zip.message}</p>)}
        </fieldset>
      </div>
    </div>
  );
}
