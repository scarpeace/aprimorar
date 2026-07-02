import type { InputHTMLAttributes } from "react";
import { get, useFormContext } from "react-hook-form";
import { Field } from "@/components/ui/forms/Field";

type TextInputProps = InputHTMLAttributes<HTMLInputElement> & {
  name: string;
  label: string;
  helperText?: string;
};

export function TextInput({ name, label, helperText, className = "", ...props }: Readonly<TextInputProps>) {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  const error = get(errors, name)?.message?.toString();

  return (
    <Field label={label} error={error} helperText={helperText}>
      <input {...register(name)} {...props} className={`input input-bordered w-full ${className}`.trim()} />
    </Field>
  );
}
