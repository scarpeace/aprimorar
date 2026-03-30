import React, { useId, useRef, useState } from "react";
import { ChevronDown } from "lucide-react";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { useParentsSummary } from "../query/parentQueries";
import type { ParentSummaryDTO } from "@/kubb";
import { ButtonLink } from "@/components/ui/button";
import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import type {
  FieldErrors,
  UseFormRegister,
  UseFormSetValue,
} from "react-hook-form";
import type { StudentFormInput } from "@/features/students/schemas/studentFormSchema";

type ParentSelectDropdownProps = Readonly<{
  selectedParentId?: string;
  register: UseFormRegister<StudentFormInput>;
  setValue: UseFormSetValue<StudentFormInput>;
  errors: FieldErrors<StudentFormInput>;
  className?: string;
}>;

export function ParentSelectDropdown({
  selectedParentId,
  register,
  setValue,
  errors,
}: ParentSelectDropdownProps) {
  const {
    data: parentSummary,
    isLoading: isLoadingParentSummary,
    error: parentSummaryError,
  } = useParentsSummary();

  const uniqueId = useId().replaceAll(":", "");
  const popoverId = `parents-list-${uniqueId}`;
  const anchorName = `--anchor-${uniqueId}`;

  const selectedParent = parentSummary?.find((p) => p.id === selectedParentId);

  return (
    <SectionCard
      title="Responsável"
      description="Selecione um responsável já cadastrado no sistema."
    >
      <div className={""}>
        <FormField
          label="Responsável"
          htmlFor="parentId"
          error={errors.parent?.message}
        >
          {/* Input oculto para que o react-hook-form consiga registrar o campo e suas validações */}
          <input type="hidden" {...register("parent")} />

          <div className="flex flex-col gap-2">
            <button
              type="button"
              className={`btn w-full justify-between ${errors.parent ? "border-error" : ""}`}
              disabled={isLoadingParentSummary}
              popoverTarget={popoverId}
              style={{ anchorName } as React.CSSProperties}
            >
              {isLoadingParentSummary
                ? "Carregando responsáveis..."
                : selectedParent?.name || "Selecione um responsável"}
              <ChevronDown className="ml-auto" />
            </button>

            <ul
              className="dropdown menu w-full max-h-60 overflow-y-auto rounded-box bg-base-100 shadow-xl z-50 flex-nowrap"
              popover="auto"
              id={popoverId}
              style={{ positionAnchor: anchorName } as React.CSSProperties}
            >
              {parentSummary?.map((parent: ParentSummaryDTO) => (
                <li key={parent.id}>
                  <button
                    type="button"
                    className={selectedParent === parent ? "active" : ""}
                    onClick={() => {
                      setValue("parent", parent, {
                        shouldValidate: true,
                        shouldDirty: true,
                      });

                      // Força o fechamento do popover após selecionar uma opção
                      const popover = document.getElementById(popoverId);
                      popover?.hidePopover?.();
                    }}
                  >
                    {parent.name}
                  </button>
                </li>
              ))}
              {parentSummary?.length === 0 && (
                <li className="disabled">
                  <span className="opacity-50 px-4 py-2">
                    Nenhum responsável encontrado
                  </span>
                </li>
              )}
            </ul>

            {parentSummaryError && (
              <p className="text-xs text-error">
                {getFriendlyErrorMessage(parentSummaryError)}
              </p>
            )}

            <p className="mt-1 text-xs text-base-content/70">
              Não encontrou o responsável?{" "}
              <ButtonLink
                to="/parents/new"
                variant="ghost"
                size="sm"
                className="h-auto p-0 underline"
              >
                Cadastre um novo aqui
              </ButtonLink>
            </p>
          </div>
        </FormField>
      </div>
    </SectionCard>
  );
}
