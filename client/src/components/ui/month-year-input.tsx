import { ptBR } from "date-fns/locale/pt-BR";
import { CalendarArrowDown } from "lucide-react";
import DatePicker, { registerLocale } from "react-datepicker";

interface MonthYearPickerProps {
  selectedDate: Date;
  onChange: (date: Date) => void;
  tooltip?: string;
}

registerLocale("pt-BR", ptBR);

export const MonthYearPicker = ({
  selectedDate,
  onChange,
  tooltip = "Selecione o mês e ano para filtrar os dados",
}: MonthYearPickerProps) => {
  return (
    <div className="tooltip tooltip-left" data-tip={tooltip}>
      <DatePicker
        showIcon
        isClearable
        icon={<CalendarArrowDown className="h-5 w-5" />}
        selected={selectedDate}
        onChange={(date: Date | null) => onChange(date ?? new Date())}
        dateFormat="MMMM/yyyy"
        locale="pt-BR"
        showMonthYearPicker
        showFullMonthYearPicker
        showTwoColumnMonthYearPicker
        className="h-10 w-full cursor-pointer rounded-xl border border-base-300 bg-base-100 px-3 text-center text-sm font-semibold text-base-content capitalize shadow-sm outline-none transition-colors hover:border-base-300 focus:border-primary/30"
        wrapperClassName="w-full"
        popperClassName="app-date-range-popper"
      />
    </div>
  );
};
