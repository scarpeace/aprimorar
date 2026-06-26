import DatePicker, { registerLocale } from "react-datepicker";
import { Field } from "./Field";
import { useState } from "react";

type DateTimePickerProps = {
  name: string;
  label: string;
  error?: string;
  // handleDateChange: (date: Date | null) => void;
  tip?: string;
};

// registerLocale("pt-BR");

export function DateTimePicker({ name, label, error, tip }: DateTimePickerProps) {
  const [date, setDate] = useState<Date | null>(null);

  return (
    <>
      <fieldset className="fieldset w-full">
        <legend className="fieldset-legend">
          {label}
        </legend>

        <DatePicker
          selected={date}
          isClearable
          clearButtonClassName="app-date-range-clear"
          onChange={setDate}
          timeInputLabel="Horário"
          locale="pt-BR"
          // timeIntervals={15}
          dateFormat="dd/MM/yyyy"
          showTimeSelect
          timeFormat="HH:mm"
          className="w-full border border-base-content/50 rounded-sm p-2 cursor-pointer bg-transparent text-sm text-base-content hover:border-base-content"
          placeholderText="dd/mm/yyyy"
          wrapperClassName="input"
          popperPlacement="top-end"
        />

        {error && (
          <p className="text-error text-xs">
            {error}
          </p>
        )}
      </fieldset>

    </>
  );
}
