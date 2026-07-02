import type { InputHTMLAttributes } from "react";
import { get, useFormContext } from "react-hook-form";
import { Field } from "@/components/ui/forms/Field";

type DateInputProps = Omit<InputHTMLAttributes<HTMLInputElement>, "type"> & {
  name: string;
  label: string;
  helperText?: string;
  dateTime?: boolean;
};

export function DateInput({ name, label, helperText, dateTime = false, className = "", ...props }: Readonly<DateInputProps>) {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  const error = get(errors, name)?.message?.toString();

  return (
    <Field label={label} error={error} helperText={helperText}>
      <input
        type={dateTime ? "datetime-local" : "date"}
        {...register(name)}
        {...props}
        className={`input input-bordered w-full ${className}`.trim()}
      />
    </Field>
  );
}
