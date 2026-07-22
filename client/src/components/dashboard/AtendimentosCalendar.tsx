"use client";

import { useEffect, useMemo, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { keepPreviousData } from "@tanstack/react-query";
import type {
  DatesSetArg,
  EventClickArg,
  EventInput,
} from "@fullcalendar/core";
import type FullCalendar from "@fullcalendar/react";
import type { CalendarioAtendimentosResponse } from "@/lib/api/generated/types/CalendarioAtendimentosResponse";
import { useGetCalendarioAtendimentos } from "@/lib/api/generated/hooks/atendimento/useGetCalendarioAtendimentos";
import { CalendarDesktop } from "@/components/dashboard/CalendarDesktop";
import { CalendarMobile } from "@/components/dashboard/CalendarMobile";
import type { SharedCalendarProps } from "@/components/dashboard/calendar-shared";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Button } from "@/components/ui/Button";
import { getFriendlyErrorMessage } from "@/lib/api/client";
import {
  getAtendimentoCalendarColor,
  tipoAtendimentoLabels,
} from "@/lib/constants/atendimento-constants";
import { toAnoMes } from "@/lib/utils/date-utils";

const MOBILE_BREAKPOINT = "(max-width: 768px)";

const MOBILE_VIEW_OPTIONS = [
  { value: "timeGridDay", label: "Dia" },
  { value: "listWeek", label: "Semana" },
  { value: "listMonth", label: "Mês" },
] as const;

const DESKTOP_VIEW_OPTIONS = [
  { value: "dayGridMonth", label: "Mês" },
  { value: "timeGridWeek", label: "Semana" },
  { value: "timeGridDay", label: "Dia" },
] as const;

function toCalendarEvent(
  atendimento: CalendarioAtendimentosResponse,
): EventInput {
  return {
    id: String(atendimento.id),
    title: `${atendimento.nomeAluno} - ${atendimento.nomeColaborador}`,
    start: atendimento.dataHoraInicio,
    end: atendimento.dataHoraFim,
    color: getAtendimentoCalendarColor(atendimento.tipo).backgroundColor,
    extendedProps: {
      nomeAluno: atendimento.nomeAluno,
      nomeColaborador: atendimento.nomeColaborador,
    },
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
    {
      tipo: "AULA",
      label: tipoAtendimentoLabels.AULA,
      total: data?.totalAulas ?? 0,
    },
    {
      tipo: "MENTORIA",
      label: tipoAtendimentoLabels.MENTORIA,
      total: data?.totalMentoria ?? 0,
    },
    {
      tipo: "TERAPIA",
      label: tipoAtendimentoLabels.TERAPIA,
      total: data?.totalTerapia ?? 0,
    },
    {
      tipo: "ORIENTACAO_VOCACIONAL",
      label: tipoAtendimentoLabels.ORIENTACAO_VOCACIONAL,
      total: data?.totalOV ?? 0,
    },
    {
      tipo: "ENEM",
      label: tipoAtendimentoLabels.ENEM,
      total: data?.totalENEM ?? 0,
    },
    {
      tipo: "PAS",
      label: tipoAtendimentoLabels.PAS,
      total: data?.totalPAS ?? 0,
    },
    {
      tipo: "OUTRO",
      label: tipoAtendimentoLabels.OUTRO,
      total: data?.totalOutros ?? 0,
    },
  ];
}

export function AtendimentosCalendar() {
  const [calendarDate, setCalendarDate] = useState(() => new Date());
  const [isMobile, setIsMobile] = useState(false);
  const [calendarTitle, setCalendarTitle] = useState("");
  const [currentView, setCurrentView] = useState("dayGridMonth");
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

  useEffect(() => {
    const mediaQuery = window.matchMedia(MOBILE_BREAKPOINT);
    const sync = () => setIsMobile(mediaQuery.matches);

    sync();
    mediaQuery.addEventListener("change", sync);

    return () => mediaQuery.removeEventListener("change", sync);
  }, []);

  function handleOpenDay(date: Date) {
    setCalendarDate(date);
    calendarRef.current?.getApi().changeView("timeGridDay", date);
  }

  function handleEventClick(info: EventClickArg) {
    info.jsEvent.preventDefault();
    router.push(`/atendimentos/${info.event.id}`);
  }

  function handleDatesSet(info: DatesSetArg) {
    setCalendarTitle(info.view.title);
    setCurrentView(info.view.type);

    const next = info.view.currentStart.getTime();
    const current = calendarDate.getTime();

    if (next !== current) {
      setCalendarDate(info.view.currentStart);
    }
  }

  function handlePrevious() {
    calendarRef.current?.getApi().prev();
  }

  function handleNext() {
    calendarRef.current?.getApi().next();
  }

  function handleToday() {
    calendarRef.current?.getApi().today();
  }

  function handleChangeView(view: string) {
    calendarRef.current?.getApi().changeView(view);
  }

  const sharedCalendarProps: SharedCalendarProps = {
    calendarRef,
    calendarDate,
    events: calendarEvents,
    onDatesSet: handleDatesSet,
    onEventClick: handleEventClick,
    onOpenDay: handleOpenDay,
  };
  const viewOptions = isMobile ? MOBILE_VIEW_OPTIONS : DESKTOP_VIEW_OPTIONS;

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
        <p className="text-sm text-base-content/60">
          Carregando atendimentos...
        </p>
      </section>
    );
  }

  return (
    <section className="rounded-2xl border border-base-300 bg-base-100 p-6 shadow-sm">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
        <div>
          <div className="flex items-center gap-3">
            <h2 className="text-2xl font-bold text-base-content">
              Calendário de atendimentos
            </h2>
            <span className="badge badge-primary badge-soft">
              {data?.totalAtendimentos ?? 0}
            </span>
          </div>
          <p className="mt-2 text-sm text-base-content/65">
            Visualize os atendimentos agendados e clique em um evento para abrir
            o detalhe.
          </p>
        </div>

        <div className="flex flex-wrap gap-2">
          {items.map((item) => (
            <div
              key={item.tipo}
              className="badge badge-soft h-auto gap-2 px-3 py-2 text-xs text-base-content/75"
            >
              <span
                className="h-2.5 w-2.5 rounded-full"
                style={{
                  backgroundColor: getAtendimentoCalendarColor(item.tipo)
                    .backgroundColor,
                }}
              />
              <span>{item.label}</span>
              <span className="font-semibold">{item.total}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="appointments-calendar mt-6 rounded-2xl border border-base-300 bg-base-100 p-3">
        <div className="mb-4 space-y-3 border-b border-base-300 pb-3">
          <div className="flex items-center gap-2">
            <Button
              type="button"
              variant="ghost"
              size="sm"
              className="btn-square"
              aria-label="Período anterior"
              onClick={handlePrevious}
            >
              ‹
            </Button>

            <div className="min-w-0 flex-1 text-center">
              <p className="truncate text-sm font-semibold text-base-content sm:text-base">
                {calendarTitle}
              </p>
            </div>

            <Button
              type="button"
              variant="ghost"
              size="sm"
              className="px-3"
              onClick={handleToday}
            >
              Hoje
            </Button>

            <Button
              type="button"
              variant="ghost"
              size="sm"
              className="btn-square"
              aria-label="Próximo período"
              onClick={handleNext}
            >
              ›
            </Button>
          </div>

          <div className="join flex w-full">
            {viewOptions.map((option) => {
              const isActive = currentView === option.value;

              return (
                <button
                  key={option.value}
                  type="button"
                  className={`join-item btn btn-sm min-w-0 flex-1 ${
                    isActive ? "btn-primary" : "btn-ghost"
                  }`}
                  onClick={() => handleChangeView(option.value)}
                >
                  <span className="truncate">{option.label}</span>
                </button>
              );
            })}
          </div>
        </div>

        {isMobile ? (
          <CalendarMobile {...sharedCalendarProps} />
        ) : (
          <CalendarDesktop {...sharedCalendarProps} />
        )}
      </div>
    </section>
  );
}
