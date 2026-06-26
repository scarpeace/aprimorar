import { Button } from "@/components/Ui/Button.tsx";
import { monthTabs } from "@/utils/constants/atendimento-constants.ts";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { Fragment, type ReactNode } from "react";

type AtendimentosPeriodTabsProps = {
  selectedYear: number;
  selectedMonthIndex: number;
  monthTabsGroup: string;
  onPrevYear: () => void;
  onNextYear: () => void;
  onMonthChange: (monthIndex: number) => void;
  children: ReactNode;
};

export function AtendimentosTablePeriodTabs({
  selectedYear,
  selectedMonthIndex,
  monthTabsGroup,
  onPrevYear,
  onNextYear,
  onMonthChange,
  children,
}: AtendimentosPeriodTabsProps) {
  return (
    <section className="mb-4 overflow-hidden rounded-2xl border border-base-300 bg-base-100 shadow-sm">
      <div className="flex items-center justify-center gap-2 border-b border-base-300 px-3 py-3 sm:gap-3">
        <Button type="button" variant="ghost" size="sm" className="btn-square" aria-label="Ano anterior" onClick={onPrevYear}>
          <ChevronLeft className="h-4 w-4" />
        </Button>

        <span className="min-w-20 text-center text-base font-semibold text-base-content">
          {selectedYear}
        </span>

        <Button type="button" variant="ghost" size="sm" className="btn-square" aria-label="Próximo ano" onClick={onNextYear}>
          <ChevronRight className="h-4 w-4" />
        </Button>
      </div>

      <div className="overflow-x-auto">
        <div className="tabs tabs-lift px-2 pt-2 justify-between">
          {monthTabs.map((month, monthIndex) => {
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
                <div className="tab-content border-base-300 bg-base-100 px-0 pt-4">
                  {isActive ? children : null}
                </div>
              </Fragment>
            );
          })}
        </div>
      </div>
    </section>
  );
}
