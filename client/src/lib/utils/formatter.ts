export const brl = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

export function formatDate(value?: string | null): string {
  if (!value) {
    return "—";
  }

  return new Intl.DateTimeFormat("pt-BR").format(new Date(value));
}

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

export function formatZip(value: string): string {
  const cleaned = value.replace(/\D/g, "");
  if (cleaned.length === 8) {
    return cleaned.replace(/(\d{5})(\d{3})/, "$1-$2");
  }
  return value;
}
