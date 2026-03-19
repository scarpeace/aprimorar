import React, { useId } from "react";
import { ChevronDown } from "lucide-react";
import { useParentsListQuery } from "../query/useParentQueries";
import type { ParentResponse } from "@/features/parents/schemas/parent";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

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
  const {
    data: parentsList,
    isLoading: isParentsListLoading,
    error: parentsListQueryError,
  } = useParentsListQuery();

  const uniqueId = useId().replaceAll(":", "");
  const popoverId = `parents-list-${uniqueId}`;
  const anchorName = `--anchor-${uniqueId}`;

  const selectedParent = parentsList?.find((p) => p.id === value);
  const activeParents = parentsList?.filter((p) => p.archivedAt === null);

  return (
    <div className="flex flex-col gap-2">
      <div className="dropdown w-full">
        <button
          type="button"
          className={`btn w-full justify-between ${hasError ? "border-error" : ""}`}
          disabled={disabled || isParentsListLoading}
          popoverTarget={popoverId}
          style={{ anchorName } as React.CSSProperties}
        >
          {isParentsListLoading
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
          {activeParents?.map((parent: ParentResponse) => (
            <li key={parent.id}>
              <a
                role="button"
                tabIndex={0}
                className={value === parent.id ? "active" : ""}
                onClick={() => {
                  onChange(parent.id);
                  const popover = document.getElementById(popoverId);
                  popover?.hidePopover?.();
                }}
              >
                {parent.name}
              </a>
            </li>
          ))}
          {activeParents?.length === 0 && (
            <li className="disabled">
              <span className="opacity-50 px-4 py-2">
                Nenhum responsável encontrado
              </span>
            </li>
          )}
        </ul>
      </div>

      {parentsListQueryError && (
        <p className="text-xs text-error">
          {getFriendlyErrorMessage(parentsListQueryError)}
        </p>
      )}
    </div>
  );
}
