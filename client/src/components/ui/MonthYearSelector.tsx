"use client";

import { Fragment, useId } from "react";
import { Button } from "@/components/ui/Button";
import { atendimentoMonthTabs } from "@/lib/constants/atendimento-constants";

type MonthYearSelectorProps = {
  selectedYear: number;
  selectedMonthIndex: number;
  onPreviousYear: () => void;
  onNextYear: () => void;
  onMonthChange: (monthIndex: number) => void;
};

export function MonthYearSelector({
  selectedYear,
  selectedMonthIndex,
  onPreviousYear,
  onNextYear,
  onMonthChange,
}: Readonly<MonthYearSelectorProps>) {
  const monthTabsGroup = useId();

  return (
    <section className="rounded-2xl border border-base-300 bg-base-100 shadow-sm">
      <div className="flex items-center justify-center gap-2 border-b border-base-300 px-3 py-3 sm:gap-3">
        <Button type="button" variant="ghost" size="sm" className="btn-square" aria-label="Ano anterior" onClick={onPreviousYear}>
          ‹
        </Button>

        <span className="min-w-20 text-center text-2xl font-semibold text-base-content">{selectedYear}</span>

        <Button type="button" variant="ghost" size="sm" className="btn-square" aria-label="Próximo ano" onClick={onNextYear}>
          ›
        </Button>
      </div>

      <div className="tabs tabs-lift justify-between pt-2">
        {atendimentoMonthTabs.map((month, monthIndex) => {
          const isActive = monthIndex === selectedMonthIndex;

          return (
            <Fragment key={month}>
              <input
                type="radio"
                name={monthTabsGroup}
                className="tab whitespace-nowrap"
                aria-label={month}
                checked={isActive}
                onChange={() => onMonthChange(monthIndex)}
              />

              <div className="tab-content rounded-xl border-base-300" />
            </Fragment>
          );
        })}
      </div>
    </section>
  );
}
