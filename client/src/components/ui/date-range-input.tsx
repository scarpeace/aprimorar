import DatePicker, { registerLocale } from "react-datepicker";
import { ptBR } from "date-fns/locale/pt-BR";
import "react-datepicker/dist/react-datepicker.css";
import { Calendar } from "lucide-react";

registerLocale("pt-BR", ptBR);

interface DateRangeInputProps {
  startDate?: Date | null;
  endDate?: Date | null;
  onStartDateChange: (date: Date | null) => void;
  onEndDateChange: (date: Date | null) => void;
}

export const DateRangeInput = ({startDate,endDate,onStartDateChange,onEndDateChange}: DateRangeInputProps) => {
  return (
    <div className="flex items-center gap-2 w-full lg:w-auto bg-base-100 rounded-md border border-base-400 px-3 py-1.5 h-10 hover:border-base-300 hover:bg-base-200 transition-colors z-20">
      <Calendar className="w-4 h-4 text-base-content/60" />
      <DatePicker
        selected={startDate}
        fixedHeight
        onChange={(date: Date | null) => onStartDateChange(date)}
        locale="pt-BR"
        dateFormat="dd/MM/yyyy"
        className="w-24 text-center font-bold bg-transparent text-sm outline-none cursor-pointer"
        placeholderText="Início"
      />
      <span className="text-base-content/50 text-sm font-medium">até</span>
      <DatePicker
        selected={endDate}
        fixedHeight
        onChange={(date: Date | null) => onEndDateChange(date)}
        minDate={startDate ? startDate : undefined}
        locale="pt-BR"
        dateFormat="dd/MM/yyyy"
        className="w-24 text-center font-bold bg-transparent text-sm outline-none cursor-pointer"
        placeholderText="Fim"
      />
    </div>
  );
};
