"use client";

import type { DatesSetArg, EventClickArg, EventContentArg, EventInput } from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import ptBrLocale from "@fullcalendar/core/locales/pt-br";
import interactionPlugin from "@fullcalendar/interaction";
import FullCalendar from "@fullcalendar/react";
import { useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSpinner } from "@/components/ui/LoadingSpinner";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";
import { useBuscarCalendarioAtendimentosIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarCalendarioAtendimentosIndividuais";
import { atendimentoTipoCalendarClass } from "@/lib/constants/atendimento-constants";
import { formatDateTimeLocal } from "@/lib/utils/date-utils";

type CalendarRange = {
  inicio: string;
  fim: string;
};

function renderEventContent(eventInfo: EventContentArg) {
  const { colaboradorNome, alunoNome } = eventInfo.event.extendedProps;

  return (
    <div className="atendimento-event-content">
      <span className="atendimento-event-time">{eventInfo.timeText}</span>
      <span className="atendimento-event-colaborador">{colaboradorNome}</span>
      <span className="atendimento-event-aluno">{alunoNome}</span>
    </div>
  );
}

function getInitialRange(): CalendarRange {
  const now = new Date();
  const inicio = new Date(now.getFullYear(), now.getMonth(), 1);
  const fim = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59);

  return {
    inicio: formatDateTimeLocal(inicio),
    fim: formatDateTimeLocal(fim),
  };
}

export function AtendimentosCalendar() {
  const router = useRouter();
  const [range, setRange] = useState<CalendarRange>(getInitialRange);
  const calendario = useBuscarCalendarioAtendimentosIndividuais(range);

  const events = useMemo<EventInput[]>(
    () =>
      (calendario.data ?? []).map((atendimento) => ({
        id: String(atendimento.id),
        title: `${atendimento.alunoNome} - ${atendimento.colaboradorNome}`,
        start: atendimento.dataHoraInicio,
        end: atendimento.dataHoraFim,
        classNames: [
          "atendimento-event",
          atendimentoTipoCalendarClass[atendimento.tipo] ?? "atendimento-event--outro",
        ],
        extendedProps: {
          colaboradorNome: atendimento.colaboradorNome,
          alunoNome: atendimento.alunoNome,
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

  if (calendario.isError) {
    return (
      <ErrorCard
        title="Não foi possível carregar o calendário"
        description={getFriendlyErrorMessage(calendario.error)}
        error={calendario.error}
      />
    );
  }

  if (calendario.isLoading) {
    return (
      <section className="flex min-h-96 items-center justify-center rounded-2xl border border-base-300 bg-base-100 p-6 shadow-sm">
        <LoadingSpinner />
      </section>
    );
  }

  return (
    <section className="rounded-2xl border border-base-300 bg-base-100 p-6 shadow-sm">
      <div className="relative">
        {calendario.isFetching ? (
          <div className="absolute right-0 top-0 z-10 flex items-center gap-2 rounded-lg bg-base-100/90 px-3 py-2 text-xs text-base-content/65 shadow-sm">
            <LoadingSpinner />
            Atualizando...
          </div>
        ) : null}

        <FullCalendar
          plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
          eventContent={renderEventContent}
          initialView="dayGridMonth"
          locale={ptBrLocale}
          headerToolbar={{
            left: "prev,next today",
            center: "title",
            right: "dayGridMonth,timeGridWeek,timeGridDay",
          }}
          events={events}
          datesSet={handleDatesSet}
          eventClick={handleEventClick}
          height="auto"
        />
      </div>
    </section>
  );
}
