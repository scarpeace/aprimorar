import DatePicker, { registerLocale } from "react-datepicker";

type DateTimePickerProps = {
  name: string;
  label: string;
  error?: string;
  date?: Date | null;
  handleDateChange: (date: Date | null) => void;
  tip?: string;
};

export function DateTimePicker({ name, label, error, date, handleDateChange, tip }: DateTimePickerProps) {
  return (
    <>
      <div className="tooltip" data-tip={tip}>
        <DatePicker
          selected={date}
          isClearable
          clearButtonClassName="app-date-range-clear"
          onChange={handleDateChange}
          timeInputLabel="Horário"
          locale="pt-BR"
          // timeIntervals={15}
          dateFormat="dd/MM/yyy hh:mm"
          showTimeInput
          timeFormat="24h"
          className="w-full border border-base-content/50 rounded-sm p-2 cursor-pointer bg-transparent text-sm text-base-content hover:border-base-content"
          placeholderText="dd/mm/yyyy"
          wrapperClassName="w-full"
        />
      </div>
      {error && <span>{error}</span>}
    </>
  );
}
