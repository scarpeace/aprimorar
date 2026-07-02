"use client";

import { useMemo, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { keepPreviousData } from "@tanstack/react-query";
import type { DatesSetArg, EventClickArg, EventInput } from "@fullcalendar/core";
import ptBrLocale from "@fullcalendar/core/locales/pt-br";
import dayGridPlugin from "@fullcalendar/daygrid";
import type { DateClickArg } from "@fullcalendar/interaction";
import interactionPlugin from "@fullcalendar/interaction";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import type { CalendarioAtendimentosResponse } from "@/lib/api/generated/types/CalendarioAtendimentosResponse";
import { useGetCalendarioAtendimentos } from "@/lib/api/generated/hooks/atendimento/useGetCalendarioAtendimentos";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { getFriendlyErrorMessage } from "@/lib/api/client";
import { getAtendimentoCalendarColor, tipoAtendimentoLabels } from "@/lib/constants/atendimento-constants";
import { toAnoMes } from "@/lib/utils/date-utils";

const CALENDAR_PLUGINS = [dayGridPlugin, timeGridPlugin, interactionPlugin];

const HEADER_TOOLBAR = {
  left: "prev,next dayGridMonth,timeGridWeek,timeGridDay",
  center: "title",
  right: "today",
};

const BUTTON_TEXT = {
  today: "hoje",
  month: "mês",
  week: "semana",
  day: "dia",
};

function toCalendarEvent(atendimento: CalendarioAtendimentosResponse): EventInput {
  return {
    id: String(atendimento.id),
    title: `${atendimento.nomeAluno} - ${atendimento.nomeColaborador}`,
    start: atendimento.dataHoraInicio,
    end: atendimento.dataHoraFim,
    color: getAtendimentoCalendarColor(atendimento.tipo).backgroundColor,
  };
}

function legendItems(data?: {
  totalAulas: number;
  totalMentoria: number;
  totalTerapia: number;
  totalOV: number;
  totalENEM: number;
  totalPAS: number;
  totalOutros: number;
}) {
  return [
    { tipo: "AULA", label: tipoAtendimentoLabels.AULA, total: data?.totalAulas ?? 0 },
    { tipo: "MENTORIA", label: tipoAtendimentoLabels.MENTORIA, total: data?.totalMentoria ?? 0 },
    { tipo: "TERAPIA", label: tipoAtendimentoLabels.TERAPIA, total: data?.totalTerapia ?? 0 },
    { tipo: "ORIENTACAO_VOCACIONAL", label: tipoAtendimentoLabels.ORIENTACAO_VOCACIONAL, total: data?.totalOV ?? 0 },
    { tipo: "ENEM", label: tipoAtendimentoLabels.ENEM, total: data?.totalENEM ?? 0 },
    { tipo: "PAS", label: tipoAtendimentoLabels.PAS, total: data?.totalPAS ?? 0 },
    { tipo: "OUTRO", label: tipoAtendimentoLabels.OUTRO, total: data?.totalOutros ?? 0 },
  ];
}

export function AtendimentosCalendar() {
  const [calendarDate, setCalendarDate] = useState(() => new Date());
  const calendarRef = useRef<FullCalendar>(null);
  const router = useRouter();
  const anoMes = toAnoMes(calendarDate);
  const calendarioAtendimentos = useGetCalendarioAtendimentos(
    { anoMes },
    {
      query: {
        placeholderData: keepPreviousData,
      },
    },
  );
  const data = calendarioAtendimentos.data;

  const calendarEvents = useMemo(
    () => (data?.eventos ?? []).map(toCalendarEvent),
    [data?.eventos],
  );
  const items = useMemo(() => legendItems(data), [data]);

  function handleDateClick(info: DateClickArg) {
    setCalendarDate(info.date);
    calendarRef.current?.getApi().changeView("timeGridDay", info.date);
  }

  function handleEventClick(info: EventClickArg) {
    info.jsEvent.preventDefault();
    router.push(`/atendimentos/${info.event.id}`);
  }

  function handleDatesSet(info: DatesSetArg) {
    const next = info.view.currentStart.getTime();
    const current = calendarDate.getTime();

    if (next !== current) {
      setCalendarDate(info.view.currentStart);
    }
  }

  if (calendarioAtendimentos.isError) {
    return (
      <ErrorCard
        title="Não foi possível carregar o calendário de atendimentos"
        description={getFriendlyErrorMessage(calendarioAtendimentos.error)}
        error={calendarioAtendimentos.error}
      />
    );
  }

  if (calendarioAtendimentos.isLoading && !data) {
    return (
      <section className="rounded-2xl border border-base-300 bg-base-100 p-6 shadow-sm">
        <p className="text-sm text-base-content/60">Carregando atendimentos...</p>
      </section>
    );
  }

  return (
    <section className="rounded-2xl border border-base-300 bg-base-100 p-6 shadow-sm">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
        <div>
          <div className="flex flex-wrap items-center gap-3">
            <h2 className="text-2xl font-bold text-base-content">Calendário de atendimentos</h2>
            <span className="badge badge-primary badge-soft">{data?.totalAtendimentos ?? 0}</span>
          </div>
          <p className="mt-2 text-sm text-base-content/65">
            Visualize os atendimentos agendados e clique em um evento para abrir o detalhe.
          </p>
        </div>

        <div className="flex flex-wrap gap-2">
          {items.map((item) => (
            <div key={item.tipo} className="badge badge-soft h-auto gap-2 px-3 py-2 text-xs text-base-content/75">
              <span className="h-2.5 w-2.5 rounded-full" style={{ backgroundColor: getAtendimentoCalendarColor(item.tipo).backgroundColor }} />
              <span>{item.label}</span>
              <span className="font-semibold">{item.total}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="appointments-calendar mt-6 rounded-2xl border border-base-300 bg-base-100 p-3">
        <FullCalendar
          ref={calendarRef}
          locale={ptBrLocale}
          plugins={CALENDAR_PLUGINS}
          initialView="dayGridMonth"
          initialDate={calendarDate}
          headerToolbar={HEADER_TOOLBAR}
          buttonText={BUTTON_TEXT}
          events={calendarEvents}
          eventDisplay="list-item"
          eventClick={handleEventClick}
          dateClick={handleDateClick}
          datesSet={handleDatesSet}
          allDaySlot={false}
          height="auto"
          dayMaxEvents={3}
          moreLinkText="mais"
          slotMinTime="07:00:00"
          slotMaxTime="21:00:00"
        />
      </div>
    </section>
  );
}
