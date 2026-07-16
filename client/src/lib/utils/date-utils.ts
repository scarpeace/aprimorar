import { addMinutes, differenceInMinutes, format, isValid, parseISO } from "date-fns";

function toDate(value: string | Date): Date {
  return typeof value === "string" ? parseISO(value) : value;
}

export function formatDateShortYear(date: string | Date): string {
  const value = toDate(date);

  if (!isValid(value)) {
    return "";
  }

  return value.toLocaleDateString("pt-BR");
}

export function formatTime(date: string | Date): string {
  const value = toDate(date);

  if (!isValid(value)) {
    return "";
  }

  return value.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
}

export function formatDateTimeLocal(date: string | Date): string {
  const value = toDate(date);

  if (!isValid(value)) {
    return "";
  }

  return format(value, "yyyy-MM-dd'T'HH:mm");
}

export function toAnoMes(date: string | Date): string {
  const value = toDate(date);

  if (!isValid(value)) {
    return "";
  }

  return format(value, "yyyy-MM");
}

export function addHoursToDateTimeLocal(value: string, hours?: number): string {
  if (!value || !hours || hours <= 0) {
    return "";
  }

  const date = parseISO(value);

  if (!isValid(date)) {
    return "";
  }

  return formatDateTimeLocal(addMinutes(date, hours * 60));
}

export function getDurationInHours(start: string | Date, end: string | Date): number {
  const startDate = toDate(start);
  const endDate = toDate(end);

  return differenceInMinutes(endDate, startDate) / 60;
}

export function formatDateForInput(date?: string | null): string {
  if (!date) {
    return "";
  }

  const [year, month, day] = date.split("-");
  if (!year || !month || !day) {
    return date;
  }

  return `${day}/${month}/${year}`;
}
