import { IMaskInput } from "react-imask";
import { Controller, get, useFormContext } from "react-hook-form";

import { Field } from "./Field";

type Props = {
  name: string;
  label: string;
  mask: string;
  placeholder?: string;
};

export function MaskedInput({
  name,
  label,
  mask,
  placeholder,
}: Props) {
  const { control, formState: { errors }} = useFormContext();

  const error = get(errors, name)?.message?.toString();

  return (
    <Field label={label} error={error}>
      <Controller
        name={name}
        control={control}
        render={({ field }) => (
          <IMaskInput
            mask={mask}
            value={field.value ?? ""}
            onAccept={(value: string) => field.onChange(value)}
            onBlur={field.onBlur}
            inputRef={field.ref}
            className="input w-full"
            placeholder={placeholder}
          />
        )}
      />
    </Field>
  );
}
