import { Controller, get, useFormContext } from "react-hook-form";
import { Field } from "@/components/ui/forms/Field";

type MaskedInputProps = {
  name: string;
  label: string;
  mask: string;
  placeholder?: string;
  helperText?: string;
  disabled?: boolean;
};

function applyMask(rawValue: string, mask: string) {
  const digits = rawValue.replace(/\D/g, "");
  let index = 0;
  let result = "";

  for (const char of mask) {
    if (char === "0") {
      if (index >= digits.length) {
        break;
      }

      result += digits[index];
      index += 1;
      continue;
    }

    if (index < digits.length) {
      result += char;
    }
  }

  return result;
}

export function MaskedInput({
  name,
  label,
  mask,
  placeholder,
  helperText,
  disabled,
}: Readonly<MaskedInputProps>) {
  const {
    control,
    formState: { errors },
  } = useFormContext();

  const error = get(errors, name)?.message?.toString();

  return (
    <Field label={label} error={error} helperText={helperText}>
      <Controller
        name={name}
        control={control}
        render={({ field }) => (
          <input
            ref={field.ref}
            value={applyMask(String(field.value ?? ""), mask)}
            onBlur={field.onBlur}
            onChange={(event) => field.onChange(applyMask(event.target.value, mask))}
            placeholder={placeholder}
            disabled={disabled}
            className="input input-bordered w-full"
          />
        )}
      />
    </Field>
  );
}
