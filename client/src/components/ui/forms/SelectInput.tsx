import { Controller, get, useFormContext } from "react-hook-form";
import { Field } from "@/components/ui/forms/Field";

type Option = {
  value: string;
  label: string;
};

type SelectInputProps = {
  name: string;
  label: string;
  options: Option[];
  helperText?: string;
  placeholder?: string;
  disabled?: boolean;
};

export function SelectInput({
  name,
  label,
  options,
  helperText,
  placeholder,
  disabled,
}: Readonly<SelectInputProps>) {
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
          <select {...field} ref={field.ref} onBlur={field.onBlur} disabled={disabled} className="select select-bordered w-full">
            {placeholder ? <option value="">{placeholder}</option> : null}
            {options.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        )}
      />
    </Field>
  );
}
