import type { InputHTMLAttributes } from "react";
import type { UseFormRegisterReturn } from "react-hook-form";

import { Field } from "./Field";

type DateInputProps = InputHTMLAttributes<HTMLInputElement> & {
  name: string;
  label: string;
  dateTime?: boolean;
  error?: string;
  registration?: UseFormRegisterReturn;
};

export function DateInput({ name, label, dateTime = false, error, registration, ...props }: DateInputProps) {
  return (
    <Field label={label} error={error}>
      <input
        type={dateTime ? "datetime-local" : "date"}
        className="input w-full"
        {...registration}
        {...props}
        name={name}
      />
    </Field>
  );
}
