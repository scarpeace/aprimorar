import { Controller, useFormContext } from "react-hook-form";
import { Field } from "./Field";

type SelectInputProps = {
  name: string;
  label: string;
  options: { value: string; label: string }[];
  className?: string;
};

export function SelectInput({name,label,options,className}: SelectInputProps) {

  const { control, formState: { errors } } = useFormContext();
  const error = errors[name]?.message?.toString();

  return (
    <Field label={label} error={error}>
      <Controller name={name} control={control} render={({ field }) => (
          <select {...field} className={`select w-full ${className}`} >
            <option value="">
              Selecione
            </option>

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
