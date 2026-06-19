import { useFormContext } from "react-hook-form";

const variants = {
  default: 'default',
  outlined: 'outlined',
  filled: 'filled',

};

type FieldSetProps = {
  className?: string;
  legend: string;
  placeholder: string;
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  color?: 'neutral' | 'error' | 'warning';
  disabled?: boolean;
  type?: 'text' | 'password' | 'email' | 'number' | 'date' | 'time' | 'datetime-local' | 'month' | 'week' | 'time';
  name: string;
};

export function FieldSet({
  className,
  legend,
  placeholder,
  size = "md",
  color = "neutral",
  type = 'text',
  name,
}: FieldSetProps) {

  const { register, formState: { errors } } = useFormContext();

  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend">{ legend }</legend>
      <input type={type}
        className={`input input-${size} input-${errors[name] ? 'error' : color}`}
        placeholder={placeholder}
        {...register(name)}
      />

      {errors[name] && (
        <span className="text-error text-sm">
          {String(errors[name]?.message)}
        </span>
      )}

    </fieldset>
  );
}
