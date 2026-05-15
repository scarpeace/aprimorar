import { fromDateToDatetimeLocalInput } from "@/lib/utils/dateFormatter";
import { Calendar } from "lucide-react";
import DatePicker from "react-datepicker";
import { Controller, type Control, type FieldValues, type Path } from "react-hook-form";

type DateTimeInputProps<TFieldValues extends FieldValues> = {
  placeholderText?: string;
  control: Control<TFieldValues>;
  name: Path<TFieldValues>;
};

function getDefaultTimeClassName(time: Date) {
  const hour = time.getHours();
  if (hour < 8) return "text-base-content/40";
  if (hour < 12) return "text-primary font-medium";
  if (hour <= 17) return "text-success font-medium";
  return "text-base-content";
}

export function DateTimeInput<TFieldValues extends FieldValues>({
  placeholderText,
  control,
  name,
}: Readonly<DateTimeInputProps<TFieldValues>>) {
  function setTime(date: Date, hours: number, minutes: number) {
    const next = new Date(date);
    next.setHours(hours, minutes, 0, 0);
    return next;
  }

  return (
    <Controller
      control={control}
      name={name}
      render={({ field }) => (
        <DatePicker
          selected={field.value ? new Date(field.value) : null}
          onChange={(date: Date | null) => field.onChange(fromDateToDatetimeLocalInput(date))}
          locale="pt-BR"
          showTimeSelect
          showIcon
          icon={<Calendar className="h-4 w-4" />}
          timeFormat="HH:mm"
          minTime={setTime(new Date(), 8, 0)}
          maxTime={setTime(new Date(), 17, 0)}
          timeIntervals={30}
          timeClassName={getDefaultTimeClassName}
          dateFormat="Pp"
          placeholderText={placeholderText}
          className="h-10 w-full rounded-xl border border-base-300 bg-base-100 px-3 text-sm font-medium text-base-content shadow-sm outline-none transition-colors hover:border-base-300 focus:border-primary/30"
        />
      )}
    />
  );
}
