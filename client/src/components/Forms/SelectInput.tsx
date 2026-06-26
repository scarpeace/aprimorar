import { Controller, get, useFormContext } from "react-hook-form";
import { Field } from "./Field";

type SelectInputProps = {
  name: string;
  label: string;
  options: { value: string; label: string }[];
  className?: string;
};

export function SelectInput({name,label,options,className}: SelectInputProps) {
  const { control, formState: { errors } } = useFormContext();
  const error = get(errors, name)?.message?.toString();

  return (
    <Field label={label} error={error}>
      <Controller
        name={name}
        control={control}
        render={({ field }) => (
          <select {...field} onBlur={field.onBlur} ref={field.ref} className={`select w-full ${className ?? ""}`}>

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
