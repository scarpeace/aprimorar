import { TriangleAlert } from "lucide-react";
import type { FieldError, UseFormRegister, UseFormRegisterReturn } from "react-hook-form";
import type { ZodObject } from "zod";

type FieldsetInputProps = {
  label: string;
  placeholder: string;
  registration: UseFormRegisterReturn,
  error?: string
  type: string
  className?: string;
};

export function FieldsetInput({ label, placeholder, registration, error, type, className }: FieldsetInputProps) {
  return (
    <fieldset className="fieldset">
      <legend className="fieldset-legend">{label}</legend>
      <input
        className={`input ${className}`}
        type={type}
        placeholder={placeholder}
        {...registration}
      />
      {error && (
        <p className="label text-error">
          <TriangleAlert className="w-3 h-3" />
          {error}
        </p>
      )}
    </fieldset>
  );
}
