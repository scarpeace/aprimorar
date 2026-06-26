import { get, useFormContext } from "react-hook-form";
import { Field } from "./Field";
import type { InputHTMLAttributes } from "react";

type TextInputProps =
  InputHTMLAttributes<HTMLInputElement> & {
    name: string;
    label: string;
    disabled?: boolean;
  };

export function TextInput({ name, label, disabled, ...props }: TextInputProps) {
  const { register, formState: { errors } } = useFormContext();
  const error = get(errors, name)?.message?.toString();

  return (
    <Field label={label} error={error}>
      <input {...register(name)} {...props} className="input w-full" disabled={disabled} />
    </Field>
  );
}
