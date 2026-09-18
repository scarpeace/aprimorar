import { tipoAtendimentoLabels } from "@/lib/constants/atendimento-constants";
import styles from "./AtendimentoCalendar.module.css";

export type AtendimentoCalendarEventData = {
  alunoNome: string;
  colaboradorNome: string;
  tipo: keyof typeof tipoAtendimentoLabels;
};

type AtendimentoCalendarEventContentProps = AtendimentoCalendarEventData & {
  compacto: boolean;
};

export function AtendimentoCalendarEventContent({
  alunoNome,
  colaboradorNome,
  tipo,
  compacto,
}: Readonly<AtendimentoCalendarEventContentProps>) {
  if (compacto) {
    return (
      <div className={styles.eventContentCompact}>
        <span className={styles.eventType}>{tipoAtendimentoLabels[tipo]}</span>
        <span className={styles.eventStudent}>{alunoNome}</span>
      </div>
    );
  }

  return (
    <div className={styles.eventContent}>
      <span className={styles.eventType}>{tipoAtendimentoLabels[tipo]}</span>
      <span className={styles.eventStudent}>A: {alunoNome}</span>
      <span className={styles.eventCollaborator}>C: {colaboradorNome}</span>
    </div>
  );
}
