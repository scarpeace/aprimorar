export const brl = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

export function formatPhone(value: string): string {
  const cleaned = value.replace(/\D/g, "");
  if (cleaned.length <= 11) {
    return cleaned.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
  }
  return value;
}

export function formatCpf(value: string): string {
  const cleaned = value.replace(/\D/g, "");
  if (cleaned.length <= 11) {
    return cleaned.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
  }
  return value;
}

export function formatDateShortYear(date: string | Date): string {
  const d = typeof date === "string" ? new Date(date) : date;
  return d.toLocaleDateString("pt-BR");
}

export function formatTime(date: string | Date): string {
  const d = typeof date === "string" ? new Date(date) : date;
  return d.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
}

export function formatDateTimeLocal(date: string | Date): string {
  const d = typeof date === "string" ? new Date(date) : date;
  return d.toISOString().slice(0, 16);
}

export function formatDateInputValue(date: string | Date): string {
  const d = typeof date === "string" ? new Date(date) : date;
  return d.toISOString().slice(0, 10);
}

export function formatZip(value: string): string {
  const cleaned = value.replace(/\D/g, "");
  if (cleaned.length === 8) {
    return cleaned.replace(/(\d{5})(\d{3})/, "$1-$2");
  }
  return value;
}

export function isValidCpf(cpf: string): boolean {
  const cleaned = cpf.replace(/\D/g, "");
  if (cleaned.length !== 11) return false;
  return true;
}

export function isValidBrazilianPhone(phone: string): boolean {
  const cleaned = phone.replace(/\D/g, "");
  return cleaned.length === 10 || cleaned.length === 11;
}
