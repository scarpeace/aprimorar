import { ptBR } from "date-fns/locale/pt-BR";
import { CalendarArrowDown } from "lucide-react";
import DatePicker, { registerLocale } from "react-datepicker";

interface MonthYearPickerProps {
  selectedDate: Date;
  onChange: (date: Date) => void;
}

registerLocale("pt-BR", ptBR);

export const MonthYearPicker = ({
  selectedDate,
  onChange,
}: MonthYearPickerProps) => {
  return (
      <div
        className="tooltip tooltip-left"
        data-tip="Selecione o mês e ano para filtrar os atendimentos"
      >
        <DatePicker
          showIcon
          icon={<CalendarArrowDown className="w-6 h-6" />}
          selected={selectedDate}
          onChange={(date: any) => date && onChange(date)}
          dateFormat="MM/yyyy"
          locale="pt-BR"
          showMonthYearPicker
          showFullMonthYearPicker
          showTwoColumnMonthYearPicker
          className="w-full border-3 border-base-200 rounded-xl h-10 text-md text-center font-bold hover:cursor-pointer hover:border-base-300"
        />
      </div>
  );
};
