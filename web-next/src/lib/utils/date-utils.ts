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
  return value.toISOString().slice(0, 16);
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

