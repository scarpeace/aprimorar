import type { TextareaHTMLAttributes } from "react";
import { get, useFormContext } from "react-hook-form";
import { Field } from "@/components/ui/forms/Field";

type TextAreaInputProps = TextareaHTMLAttributes<HTMLTextAreaElement> & {
  name: string;
  label: string;
  helperText?: string;
};

export function TextAreaInput({ name, label, helperText, className = "", ...props }: Readonly<TextAreaInputProps>) {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  const error = get(errors, name)?.message?.toString();

  return (
    <Field label={label} error={error} helperText={helperText}>
      <textarea {...register(name)} {...props} className={`textarea textarea-bordered w-full ${className}`.trim()} />
    </Field>
  );
}
