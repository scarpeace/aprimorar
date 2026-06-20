import type { ReactNode } from "react";

type FieldProps = {
  label: string;
  error?: string;
  helperText?: string;
  children: ReactNode;
};

export function Field({ label, error, helperText, children }: FieldProps) {

  return (
    <fieldset className="fieldset w-full">
      <legend className="fieldset-legend">
        {label}
      </legend>

      {children}

      {helperText && !error && (
        <p className="text-xs opacity-70">
          {helperText}
        </p>
      )}

      {error && (
        <p className="text-error text-xs">
          {error}
        </p>
      )}
    </fieldset>
  );
}
