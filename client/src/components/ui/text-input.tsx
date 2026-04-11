import { TriangleAlert } from "lucide-react";
import {
  useFormContext,
  type FieldError,
  type FieldValues,
  type UseFormRegister,
  type UseFormRegisterReturn,
} from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import type { ZodObject } from "zod";

type TextInputProps = {
  label: string;
  placeholder: string;
  type: string;
  className?: string;
  identifier: string;
  mask?: string[];
};

export function TextInput({
  label,
  placeholder,
  type,
  className,
  identifier,
  mask,
}: TextInputProps) {
  const { register, formState: { errors } } = useFormContext();
  const registerWithMask = useHookFormMask(register);
  return (
    <fieldset className="fieldset">
      <legend className="fieldset-legend">{label}</legend>
      <input
        className={`input ${className}`}
        type={type}
        placeholder={placeholder}
        {...(mask ? registerWithMask(identifier, mask) : register(identifier))}
      />
      {errors && (
        <p className="label text-error">
          <TriangleAlert className="w-3 h-3" />
          {errors.}
        </p>
      )}
    </fieldset>
  );
}
