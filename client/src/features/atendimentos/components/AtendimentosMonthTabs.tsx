"use client";

import { Fragment, useId, type ReactNode } from "react";
import { Button } from "@/components/ui/Button";
import { atendimentoMonthTabs } from "@/lib/constants/atendimento-constants";

type AtendimentosMonthTabsProps = {
  selectedYear: number;
  selectedMonthIndex: number;
  onPreviousYear: () => void;
  onNextYear: () => void;
  onMonthChange: (monthIndex: number) => void;
  children: ReactNode;
};

export function AtendimentosMonthTabs({
  selectedYear,
  selectedMonthIndex,
  onPreviousYear,
  onNextYear,
  onMonthChange,
  children,
}: Readonly<AtendimentosMonthTabsProps>) {
  const monthTabsGroup = useId();

  return (
    <div className="mt-6 rounded-2xl border border-base-300 bg-base-100">
      <div className="flex items-center justify-center gap-2 border-b border-base-300 px-3 py-3 sm:gap-3">
        <Button type="button" variant="ghost" size="sm" className="btn-square" aria-label="Ano anterior" onClick={onPreviousYear}>
          ‹
        </Button>

        <span className="min-w-20 text-center text-2xl font-semibold text-base-content">{selectedYear}</span>

        <Button type="button" variant="ghost" size="sm" className="btn-square" aria-label="Próximo ano" onClick={onNextYear}>
          ›
        </Button>
      </div>

      <div className="tabs tabs-lift justify-between pt-2 px-1">
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

              <div className="tab-content px-3">{isActive ? children : null}</div>
            </Fragment>
          );
        })}
      </div>
    </div>
  );
}
