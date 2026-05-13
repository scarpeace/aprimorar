import DatePicker, { registerLocale } from "react-datepicker";
import { ptBR } from "date-fns/locale/pt-BR";
import { Calendar } from "lucide-react";

registerLocale("pt-BR", ptBR);

interface DateRangeInputProps {
  startDate?: Date | null;
  endDate?: Date | null;
  onStartDateChange: (date: Date | null) => void;
  onEndDateChange: (date: Date | null) => void;
}

export const DateRangeInput = ({
  startDate,
  endDate,
  onStartDateChange,
  onEndDateChange,
}: DateRangeInputProps) => {
  return (
    <div className="app-date-range group z-20 flex w-full flex-wrap items-center gap-2 rounded-xl border border-base-300/80 bg-base-100/90 px-3 py-2 shadow-sm transition-all duration-200 hover:border-base-300 hover:shadow-md focus-within:border-primary/30 focus-within:shadow-[0_0_0_4px_color-mix(in_srgb,var(--color-primary)_8%,transparent)] lg:w-auto">
      <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-primary/8 text-primary">
        <Calendar className="w-4" />
      </div>

      <div className="flex min-w-36 flex-1 items-center rounded-lg bg-base-200/70 px-2.5 py-1.5 lg:flex-none">
        <DatePicker
          selected={startDate}
          fixedHeight
          isClearable
          clearButtonClassName="app-date-range-clear"
          onChange={(date: Date | null) => onStartDateChange(date)}
          locale="pt-BR"
          dateFormat="dd/MM/yyyy"
          className="w-full bg-transparent text-sm font-semibold text-base-content outline-none placeholder:text-base-content/35 cursor-pointer"
          placeholderText="Data inicial"
          wrapperClassName="w-full"
          popperClassName="app-date-range-popper"
        />
      </div>

      <span className="px-1 text-xs font-semibold uppercase tracking-[0.16em] text-base-content/45">
        até
      </span>

      <div className="flex min-w-36 flex-1 items-center rounded-lg bg-base-200/70 px-2.5 py-1.5 lg:flex-none">
        <DatePicker
          selected={endDate}
          fixedHeight
          isClearable
          clearButtonClassName="app-date-range-clear"
          onChange={(date: Date | null) => onEndDateChange(date)}
          minDate={startDate ? startDate : undefined}
          locale="pt-BR"
          dateFormat="dd/MM/yyyy"
          className="w-full bg-transparent text-sm font-semibold text-base-content outline-none placeholder:text-base-content/35 cursor-pointer"
          placeholderText="Data final"
          wrapperClassName="w-full"
          popperClassName="app-date-range-popper"
        />
      </div>
    </div>
  );
};
