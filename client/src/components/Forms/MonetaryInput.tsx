import type { InputHTMLAttributes } from "react";
import { Controller, useFormContext } from "react-hook-form";
import { NumericFormat } from "react-number-format";

import { Field } from "./Field";

type MonetaryInputProps = Omit<InputHTMLAttributes<HTMLInputElement>, "name" | "type"> & {
  name: string;
  label: string;
};

export function MonetaryInput({ name, label, ...props }: MonetaryInputProps) {
  const {
    control,
    formState: { errors },
  } = useFormContext();

  const error = errors[name]?.message?.toString();

  return (
    <Field label={label} error={error}>
      <Controller
        name={name}
        control={control}
        render={({ field }) => (
          <NumericFormat
            name={field.name}
            {...props}
            value={field.value ?? ""}
            getInputRef={field.ref}
            onBlur={field.onBlur}
            onValueChange={(values) => field.onChange(values.floatValue ?? undefined)}
            thousandSeparator="."
            decimalSeparator=","
            decimalScale={2}
            fixedDecimalScale
            allowNegative={false}
            prefix="R$ "
            className="input w-full"
          />
        )}
      />
    </Field>
  );
}
