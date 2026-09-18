import type { ComponentProps, ReactNode } from "react";
import { SelectInput } from "@/components/ui/forms/SelectInput";

type AsyncSelectInputProps = ComponentProps<typeof SelectInput> & {
  isLoading: boolean;
  isError: boolean;
  errorMessage?: string;
  loadingMessage?: string;
  emptyMessage?: string;
  children?: ReactNode;
};

export function AsyncSelectInput({
  isLoading,
  isError,
  errorMessage = "Não foi possível carregar as opções.",
  loadingMessage = "Carregando...",
  emptyMessage = "Nenhuma opção disponível.",
  children,
  disabled,
  options,
  ...props
}: Readonly<AsyncSelectInputProps>) {
  const isEmpty = !isLoading && !isError && options.length === 0;
  const isDisabled = disabled || isLoading || isError || isEmpty;

  return (
    <div>
      <SelectInput {...props} options={options} disabled={isDisabled} />

      {isLoading ? (
        <p className="mt-2 text-sm text-base-content/60">{loadingMessage}</p>
      ) : isError ? (
        <p className="mt-2 text-sm text-error" role="alert">
          {errorMessage}
        </p>
      ) : isEmpty ? (
        <p className="mt-2 text-sm text-base-content/60">{emptyMessage}</p>
      ) : null}

      {children}
    </div>
  );
}
