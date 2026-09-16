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
import { getFriendlyErrorMessage } from "@/lib/api/api-error";
import { useBuscarCalendarioAtendimentosIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarCalendarioAtendimentosIndividuais";
import { atendimentoTipoCalendarClass } from "@/lib/constants/atendimento-constants";
import { formatDateTimeLocal } from "@/lib/utils/date-utils";

type CalendarRange = {
  inicio: string;
  fim: string;
};

type ColaboradorCalendarProps = {
  colaboradorId: string;
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

function renderEventContent(eventInfo: EventContentArg) {
  const { alunoNome } = eventInfo.event.extendedProps;

  return (
    <div className="atendimento-event-content">
      <span className="atendimento-event-time">{eventInfo.timeText}</span>
      <span className="atendimento-event-aluno">{alunoNome}</span>
    </div>
  );
}

export function ColaboradorCalendar({ colaboradorId }: Readonly<ColaboradorCalendarProps>) {
  const router = useRouter();
  const [range, setRange] = useState<CalendarRange>(getInitialRange);
  const calendario = useBuscarCalendarioAtendimentosIndividuais({
    ...range,
    colaboradorId,
  });

  const events = useMemo<EventInput[]>(
    () =>
      (calendario.data ?? []).map((atendimento) => ({
        id: String(atendimento.id),
        title: atendimento.alunoNome,
        start: atendimento.dataHoraInicio,
        end: atendimento.dataHoraFim,
        classNames: [
          "atendimento-event",
          atendimentoTipoCalendarClass[atendimento.tipo] ?? "atendimento-event--outro",
        ],
        extendedProps: {
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
        title="Não foi possível carregar o calendário do colaborador"
        description={getFriendlyErrorMessage(calendario.error)}
        error={calendario.error}
      />
    );
  }

  if (calendario.isLoading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Calendário</CardTitle>
        </CardHeader>
        <LoadingSkeleton className="h-64 w-full" />
      </Card>
    );
  }

  return (
    <Card className="">
      <CardHeader>
        <div>
          <CardTitle>Calendário</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">Visualize os atendimentos deste colaborador.</p>
        </div>
      </CardHeader>

      <div className="relative">
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
          height="auto"
          dayMaxEvents={3}
        />
      </div>
    </Card>
  );
}
