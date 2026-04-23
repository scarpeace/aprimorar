import { TriangleAlert } from "lucide-react";
import {
  useFormContext,
} from "react-hook-form";
import { useHookFormMask } from "use-mask-input";

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
  const {
    register,
    formState: { errors },
  } = useFormContext();
  const registerWithMask = useHookFormMask(register);
  const fieldError = identifier
    .split(".")
    .reduce<unknown>((currentError, key) => {
      if (!currentError || typeof currentError !== "object") {
        return undefined;
      }

      return currentError[key as keyof typeof currentError];
    }, errors);

  const errorMessage =
    fieldError && typeof fieldError === "object" && "message" in fieldError
      ? fieldError.message
      : undefined;

  return (
    <fieldset className="fieldset">
      <legend className="fieldset-legend">{label}</legend>
      <input
        className={`input ${className}`}
        type={type}
        placeholder={placeholder}
        {...(mask ? registerWithMask(identifier, mask) : register(identifier))}
      />
      {typeof errorMessage === "string" && (
        <p className="label text-error">
          <TriangleAlert className="w-3 h-3" />
          {errorMessage}
        </p>
      )}
    </fieldset>
  );
}
