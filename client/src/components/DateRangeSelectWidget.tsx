import { useState } from "react";
import { Calendar, SlidersHorizontal, X } from "lucide-react";

import { Button } from "./button.tsx";
import { formatDateShortYear } from "@/utils/date-utils.ts";
import type { PageDateFilter } from "@/hooks/usePageDateFilter.ts";

function getPeriodLabel(startDate?: Date, endDate?: Date) {
  if (startDate && endDate) {
    return `${formatDateShortYear(startDate)} - ${formatDateShortYear(endDate)}`;
  }

  if (startDate) {
    return `A partir de ${formatDateShortYear(startDate)}`;
  }

  if (endDate) {
    return `Ate ${formatDateShortYear(endDate)}`;
  }

  return "Todo periodo";
}

type DateRangeSelectWidgetProps = PageDateFilter;

function FilterIndicator({ hasFilters }: { hasFilters: boolean }) {
  return (
    <span className="relative flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-primary/10 text-primary">
      <Calendar className="h-5 w-5" />
      {hasFilters ? (
        <span className="absolute -right-1 -top-1 h-3 w-3 rounded-full border-2 border-base-100 bg-success" />
      ) : null}
    </span>
  );
}

export function DateRangeSelectWidget({
  startDate,
  endDate,
  handleStartDateChange,
  handleEndDateChange,
  clearDateFilters,
  hasFilters,
}: Readonly<DateRangeSelectWidgetProps>) {
  const [isOpen, setIsOpen] = useState(false);
  const periodLabel = getPeriodLabel(startDate, endDate);
  const toggleOpen = () => setIsOpen((current) => !current);
  const close = () => setIsOpen(false);

  return (
    <div className="fixed bottom-4 left-3 z-50 flex w-[calc(100vw-2rem)] flex-col items-end sm:bottom-6 sm:left-3 sm:w-auto">
      {isOpen ? (
        <section className="mb-3 w-full max-w-xl rounded-3xl border border-base-300 bg-base-100/95 p-4 shadow-2xl backdrop-blur sm:w-xl animate-[fade-up_180ms_ease-out_both]">
          <div className="mb-4 flex items-start justify-between gap-4">
            <h2 className="mt-1 text-lg font-bold text-base-content">Selecione as datas</h2>

            <Button
              type="button"
              variant="ghost"
              size="sm"
              className="btn-square"
              aria-label="Fechar filtro de periodo"
              onClick={close}
            >
              <X className="h-4 w-4" />
            </Button>
          </div>

          <div className="flex flex-col gap-3">


            <div className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
              <span className="text-xs font-medium text-base-content/55">
                Selecionado: {periodLabel}
              </span>
              {hasFilters ? (
                <Button
                  type="button"
                  variant="ghost"
                  size="sm"
                  onClick={clearDateFilters}
                >
                  Limpar periodo
                </Button>
              ) : null}
            </div>
          </div>
        </section>
      ) : null}

      <button
        type="button"
        className={`group flex max-w-full items-center gap-3 rounded-2xl border border-base-300 bg-base-100/95 px-4 py-3 text-left shadow-2xl backdrop-blur transition hover:-translate-y-0.5 hover:border-primary/35 ${hasFilters ? "ring-2 ring-primary/15" : ""}`}
        aria-expanded={isOpen}
        aria-label="Abrir filtro de periodo"
        onClick={toggleOpen}
      >
        <FilterIndicator hasFilters={hasFilters} />
        <span className="min-w-0">
          <span className="block text-[10px] font-bold uppercase tracking-[0.22em] text-base-content/45">
            Filtro de Datas
          </span>
          <span className="block truncate text-sm font-bold text-base-content sm:max-w-72">
            {periodLabel}
          </span>
        </span>
        <SlidersHorizontal className="h-4 w-4 shrink-0 text-base-content/45 transition group-hover:text-primary" />
      </button>
    </div>
  );
}
