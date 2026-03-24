import React, { useId } from "react";
import { ChevronDown } from "lucide-react";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useGetParentsOptions, type ParentOptionDTO } from "@/kubb";

type ParentSelectDropdownProps = Readonly<{
  value?: string;
  onChange: (id: string) => void;
  disabled?: boolean;
  hasError?: boolean;
}>;

export function ParentSelectDropdown({
  value,
  onChange,
  disabled,
  hasError,
}: ParentSelectDropdownProps) {

  const { data: parentOptions, isLoading: isLoadingParentOptions, error: parentOptionsError } = useGetParentsOptions()

  const uniqueId = useId().replaceAll(":", "");
  const popoverId = `parents-list-${uniqueId}`;
  const anchorName = `--anchor-${uniqueId}`;

  const selectedParent = parentOptions?.find((p) => p.id === value);

  return (
    <div className="flex flex-col gap-2">
      <div className="dropdown w-full">
        <button
          type="button"
          className={`btn w-full justify-between ${hasError ? "border-error" : ""}`}
          disabled={disabled || isLoadingParentOptions}
          popoverTarget={popoverId}
          style={{ anchorName } as React.CSSProperties}
        >
          {isLoadingParentOptions
            ? "Carregando responsáveis..."
            : selectedParent?.name || "Selecione o responsável"}
          <ChevronDown className="ml-auto" />
        </button>

        <ul
          className="dropdown menu w-full max-h-60 overflow-y-auto rounded-box bg-base-100 shadow-sm z-10"
          popover="auto"
          id={popoverId}
          style={{ positionAnchor: anchorName } as React.CSSProperties}
        >
          {parentOptions?.map((parent: ParentOptionDTO) => (
            <li key={parent.id}>
              <button
                type="button"
                className={value === parent.id ? "active" : ""}
                onClick={() => {
                  onChange(parent.id);
                  const popover = document.getElementById(popoverId);
                  popover?.hidePopover?.();
                }}
              >
                {parent.name}
              </button>
            </li>
          ))}
          {parentOptions?.length === 0 && (
            <li className="disabled">
              <span className="opacity-50 px-4 py-2">
                Nenhum responsável encontrado
              </span>
            </li>
          )}
        </ul>
      </div>

      {parentOptionsError && (
        <p className="text-xs text-error">
          {getFriendlyErrorMessage(parentOptionsError)}
        </p>
      )}
    </div>
  );
}
