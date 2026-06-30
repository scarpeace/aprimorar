import type { ReactNode } from "react";

type FieldProps = {
  label: string;
  error?: string;
  helperText?: string;
  children: ReactNode;
};

export function Field({ label, error, helperText, children }: Readonly<FieldProps>) {
  return (
    <fieldset className="fieldset w-full">
      <legend className="fieldset-legend">{label}</legend>
      {children}

      {helperText && !error ? <p className="text-xs opacity-70">{helperText}</p> : null}
      {error ? <p className="text-xs text-error">{error}</p> : null}
    </fieldset>
  );
}
