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
    <div className="app-date-range flex w-full flex-wrap items-center gap-2 rounded-xl border border-base-300 bg-base-100 px-3 py-2 shadow-sm focus-within:border-primary/30 lg:w-auto">
      <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-primary/10 text-primary">
        <Calendar className="w-4" />
      </div>

      <div className="flex min-w-36 flex-1 items-center rounded-lg bg-base-200 px-3 py-2 lg:flex-none">
        <DatePicker
          selected={startDate}
          fixedHeight
          isClearable
          clearButtonClassName="app-date-range-clear"
          onChange={(date: Date | null) => onStartDateChange(date)}
          locale="pt-BR"
          dateFormat="dd/MM/yyyy"
          className="w-full cursor-pointer bg-transparent text-sm font-medium text-base-content outline-none placeholder:text-base-content/40"
          placeholderText="Data inicial"
          wrapperClassName="w-full"
          popperClassName="app-date-range-popper"
        />
      </div>

      <span className="px-1 text-xs font-semibold uppercase tracking-[0.16em] text-base-content/45">
        até
      </span>

      <div className="flex min-w-36 flex-1 items-center rounded-lg bg-base-200 px-3 py-2 lg:flex-none">
        <DatePicker
          selected={endDate}
          fixedHeight
          isClearable
          clearButtonClassName="app-date-range-clear"
          onChange={(date: Date | null) => onEndDateChange(date)}
          minDate={startDate ? startDate : undefined}
          locale="pt-BR"
          dateFormat="dd/MM/yyyy"
          className="w-full cursor-pointer bg-transparent text-sm font-medium text-base-content outline-none placeholder:text-base-content/40"
          placeholderText="Data final"
          wrapperClassName="w-full"
          popperClassName="app-date-range-popper"
        />
      </div>
    </div>
  );
};
