import type { AtendimentoResponseDTO } from "@/kubb";

export const APPOINTMENT_CONTENT_COLORS: Record<string, { backgroundColor: string; borderColor: string }> = {
  AULA: { backgroundColor: "#3b82f6", borderColor: "#2563eb" },
  MENTORIA: { backgroundColor: "#10b981", borderColor: "#059669" },
  TERAPIA: { backgroundColor: "#f59e0b", borderColor: "#d97706" },
  ORIENTACAO_VOCACIONAL: { backgroundColor: "#8b5cf6", borderColor: "#7c3aed" },
  ENEM: { backgroundColor: "#ec4899", borderColor: "#db2777" },
  PAS: { backgroundColor: "#06b6d4", borderColor: "#0891b2" },
  OUTRO: { backgroundColor: "#6b7280", borderColor: "#4b5563" },
};

export function getAppointmentColor(atendimento: Pick<AtendimentoResponseDTO, "tipo">) {
  return APPOINTMENT_CONTENT_COLORS[atendimento.tipo] ?? APPOINTMENT_CONTENT_COLORS.OUTRO;
}
