"use client";

import type { DatesSetArg, EventClickArg, EventContentArg, EventInput } from "@fullcalendar/core";
import ptBrLocale from "@fullcalendar/core/locales/pt-br";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import FullCalendar from "@fullcalendar/react";
import { useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import styles from "@/features/atendimentos/components/calendarios/AtendimentoCalendar.module.css";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";
import { useBuscarCalendarioAtendimentosIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarCalendarioAtendimentosIndividuais";
import { atendimentoTipoCalendarClass, tipoAtendimentoLabels } from "@/lib/constants/atendimento-constants";
import { formatDateTimeLocal } from "@/lib/utils/date-utils";

type CalendarRange = {
  inicio: string;
  fim: string;
};

type AlunoCalendarProps = {
  alunoId: string;
};

type AlunoCalendarEventData = {
  colaboradorNome: string;
  tipo: keyof typeof tipoAtendimentoLabels;
};

function getInitialRange(): CalendarRange {
  const now = new Date();
  const inicio = new Date(now.getFullYear(), now.getMonth(), 1);
  const fim = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59);

  return {
    inicio: formatDateTimeLocal(inicio),
    fim: formatDateTimeLocal(fim),
  };
}

function AlunoCalendarEventContent({ colaboradorNome, tipo }: Readonly<AlunoCalendarEventData>) {
  return (
    <div className={styles.eventContent}>
      <span className={styles.eventType}>{tipoAtendimentoLabels[tipo]}</span>
      <span className={styles.eventCollaborator}>C: {colaboradorNome}</span>
    </div>
  );
}

function renderEventContent(eventInfo: EventContentArg) {
  const eventData = eventInfo.event.extendedProps as AlunoCalendarEventData;

  return <AlunoCalendarEventContent {...eventData} />;
}

export function AlunoCalendar({ alunoId }: Readonly<AlunoCalendarProps>) {
  const router = useRouter();
  const [range, setRange] = useState<CalendarRange>(getInitialRange);
  const calendario = useBuscarCalendarioAtendimentosIndividuais({
    ...range,
    alunoId,
  });

  const events = useMemo<EventInput[]>(
    () =>
      (calendario.data ?? []).map((atendimento) => ({
        id: String(atendimento.id),
        title: `${tipoAtendimentoLabels[atendimento.tipo]} — C: ${atendimento.colaboradorNome}`,
        start: atendimento.dataHoraInicio,
        end: atendimento.dataHoraFim,
        classNames: [
          "atendimento-event",
          atendimentoTipoCalendarClass[atendimento.tipo] ?? "atendimento-event--outro",
        ],
        extendedProps: {
          colaboradorNome: atendimento.colaboradorNome,
          tipo: atendimento.tipo,
        },
      })),
    [calendario.data],
  );

  function handleDatesSet(info: DatesSetArg) {
    const nextRange = {
      inicio: formatDateTimeLocal(info.start),
      fim: formatDateTimeLocal(info.end),
    };

    setRange((currentRange) => {
      if (currentRange.inicio === nextRange.inicio && currentRange.fim === nextRange.fim) {
        return currentRange;
      }

      return nextRange;
    });
  }

  function handleEventClick(info: EventClickArg) {
    router.push(`/atendimentos/${info.event.id}`);
  }

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Calendário</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">Visualize os atendimentos deste aluno.</p>
        </div>
      </CardHeader>

      {calendario.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : calendario.isError ? (
        <ErrorCard
          title="Não foi possível carregar o calendário do aluno"
          description={getFriendlyErrorMessage(calendario.error)}
          error={calendario.error}
        />
      ) : (
        <div className={styles.calendar}>
          {calendario.isFetching ? <LoadingSkeleton className="absolute right-0 top-0 z-10 h-2 w-20" /> : null}

          <FullCalendar
            plugins={[dayGridPlugin, interactionPlugin]}
            eventContent={renderEventContent}
            initialView="dayGridMonth"
            locale={ptBrLocale}
            headerToolbar={{
              left: "prev,next",
              center: "title",
              right: "today",
            }}
            buttonText={{ today: "Hoje" }}
            events={events}
            datesSet={handleDatesSet}
            eventClick={handleEventClick}
            aspectRatio={2.2}
            dayMaxEvents={3}
          />
        </div>
      )}
    </Card>
  );
}
