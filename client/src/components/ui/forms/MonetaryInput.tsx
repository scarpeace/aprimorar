import { Controller, get, useFormContext } from "react-hook-form";
import { Field } from "@/components/ui/forms/Field";

type MonetaryInputProps = {
  name: string;
  label: string;
  helperText?: string;
  placeholder?: string;
  disabled?: boolean;
};

const currencyFormatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

function parseCurrency(value: string) {
  const digits = value.replace(/\D/g, "");

  if (!digits) {
    return undefined;
  }

  return Number(digits) / 100;
}

function formatCurrency(value: unknown) {
  if (typeof value !== "number" || Number.isNaN(value)) {
    return "";
  }

  return currencyFormatter.format(value);
}

export function MonetaryInput({
  name,
  label,
  helperText,
  placeholder = "R$ 0,00",
  disabled,
}: Readonly<MonetaryInputProps>) {
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
            value={formatCurrency(field.value)}
            onBlur={field.onBlur}
            onChange={(event) => field.onChange(parseCurrency(event.target.value))}
            placeholder={placeholder}
            disabled={disabled}
            inputMode="numeric"
            className="input input-bordered w-full"
          />
        )}
      />
    </Field>
  );
}
