"use client";

import type { DatesSetArg, EventClickArg, EventInput } from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import ptBrLocale from "@fullcalendar/core/locales/pt-br";
import interactionPlugin from "@fullcalendar/interaction";
import FullCalendar from "@fullcalendar/react";
import { useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSpinner } from "@/components/ui/LoadingSpinner";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";
import { useBuscarCalendarioAtendimentosIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarCalendarioAtendimentosIndividuais";

type CalendarRange = {
  inicio: string;
  fim: string;
};

function getInitialRange(): CalendarRange {
  const now = new Date();
  const inicio = new Date(now.getFullYear(), now.getMonth(), 1);
  const fim = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59);

  return {
    inicio: inicio.toISOString(),
    fim: fim.toISOString(),
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
      })),
    [calendario.data],
  );

  function handleDatesSet(info: DatesSetArg) {
    const nextRange = {
      inicio: info.start.toISOString(),
      fim: info.end.toISOString(),
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
          plugins={[dayGridPlugin, interactionPlugin]}
          initialView="dayGridMonth"
          locale={ptBrLocale}
          headerToolbar={{
            left: "prev,next today",
            center: "title",
            right: "dayGridMonth",
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
