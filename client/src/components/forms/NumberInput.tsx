import type { InputHTMLAttributes } from "react";
import { useFormContext } from "react-hook-form";

import { Field } from "./Field";

type NumberInputProps = InputHTMLAttributes<HTMLInputElement> & {
  name: string;
  label: string;
  step?: string;
};

export function NumberInput({ name, label, step = "0.01", ...props }: NumberInputProps) {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  const error = errors[name]?.message?.toString();

  return (
    <Field label={label} error={error}>
      <input
        type="number"
        step={step}
        className="input w-full"
        {...register(name, { valueAsNumber: true })}
        {...props}
      />
    </Field>
  );
}
