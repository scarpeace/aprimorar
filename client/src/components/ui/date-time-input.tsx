import { fromDateToDatetimeLocalInput } from "@/lib/utils/dateFormater";
import { Calendar, TriangleAlert } from "lucide-react";
import DatePicker from "react-datepicker";
import { Controller } from "react-hook-form";
type DateTimeInputProps = {

  placeholderText?: string;
  control: any
  name: string;

};
function getDefaultTimeClassName(time: Date) {
  const hour = time.getHours();
  if (hour < 8) return "text-base-content/40";
  if (hour < 12) return "text-primary font-medium";
  if (hour <= 17) return "text-success font-medium";
  return "text-base-content";
}
export function DateTimeInput({
  placeholderText,
  control,
  name,
}: Readonly<DateTimeInputProps>) {
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
          onChange={(date: any) => field.onChange(fromDateToDatetimeLocalInput(date))}
          locale="pt-BR"
          showTimeSelect
          showIcon
          icon={<Calendar/>}
          timeFormat="HH:mm"
          minTime={setTime(new Date(), 8, 0)}
          maxTime={setTime(new Date(), 17, 0)}
          timeIntervals={30}
          timeClassName={getDefaultTimeClassName}
          dateFormat="Pp"
          placeholderText={placeholderText}
          className="w-full border border-base-300 h-10 text-md"
        />
      )}
    />
  )
}
