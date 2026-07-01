function pad(value: number): string {
  return String(value).padStart(2, "0");
}

export function formatDateShortYear(date: string | Date): string {
  const value = typeof date === "string" ? new Date(date) : date;
  return value.toLocaleDateString("pt-BR");
}

export function formatTime(date: string | Date): string {
  const value = typeof date === "string" ? new Date(date) : date;
  return value.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
}

export function formatDateTimeLocal(date: string | Date): string {
  const value = typeof date === "string" ? new Date(date) : date;

  return `${value.getFullYear()}-${pad(value.getMonth() + 1)}-${pad(value.getDate())}T${pad(value.getHours())}:${pad(value.getMinutes())}`;
}

export function addHoursToDateTimeLocal(value: string, hours?: number): string {
  if (!value || !hours || hours <= 0) {
    return "";
  }

  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {
    return "";
  }

  date.setMinutes(date.getMinutes() + hours * 60);
  return formatDateTimeLocal(date);
}

export function getDurationInHours(start: string | Date, end: string | Date): number {
  const startDate = typeof start === "string" ? new Date(start) : start;
  const endDate = typeof end === "string" ? new Date(end) : end;

  return (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60);
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
