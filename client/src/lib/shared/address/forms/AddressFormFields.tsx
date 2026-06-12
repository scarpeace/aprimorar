import { TriangleAlert } from "lucide-react";
import type {
  FieldErrors,
  FieldValues,
  UseFormRegister,
} from "react-hook-form";
import type { useHookFormMask } from "use-mask-input";

import type { AddressInputSchema } from "./addressSchema.ts";
import { StateSelectDropdown } from "./StateSelectDropdown.tsx";

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
        <input type="text" className="input" placeholder="Rua" {...register(withPrefix("rua"))} />
        {errors?.rua && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.rua.message}</p>)}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Número</legend>
        <input type="text" className="input" placeholder="Número" {...register(withPrefix("numero"))} />
        {errors?.numero && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.numero.message}</p>)}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Complemento</legend>
        <input type="text" className="input" placeholder="Complemento" {...register(withPrefix("complemento"))} />
        {errors?.complemento && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.complemento.message}</p>)}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Bairro</legend>
        <input type="text" className="input" placeholder="Bairro" {...register(withPrefix("bairro"))} />
        {errors?.bairro && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.bairro.message}</p>)}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Cidade</legend>
        <input type="text" className="input" placeholder="Cidade" {...register(withPrefix("cidade"))} />
        {errors?.cidade && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.cidade.message}</p>)}
      </fieldset>

      <div className="flex flex-row gap-3">
        <StateSelectDropdown
          registration={register(withPrefix("estado"))}
          error={errors?.estado?.message}
          label={"Estado"}
        />

        <fieldset className="fieldset">
          <legend className="fieldset-legend">CEP</legend>
          <input type="text" className="input" placeholder="CEP" {...registerWithMask(withPrefix("cep"), "99999-999")}/>
          {errors?.cep && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.cep.message}</p>)}
        </fieldset>
      </div>
    </div>
  );
}
