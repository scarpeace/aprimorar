import { useMemo, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import type { DatesSetArg, EventClickArg, EventInput } from "@fullcalendar/core";
import ptBrLocale from "@fullcalendar/core/locales/pt-br";
import dayGridPlugin from "@fullcalendar/daygrid";
import type { DateClickArg } from "@fullcalendar/interaction";
import interactionPlugin from "@fullcalendar/interaction";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";

import { ErrorCard } from "@/components/ui/error-card";
import { getAppointmentColor } from "@/features/atendimentos/lib/cores-tipo-atendimento";
import { useGetCalendarioAtendimentos, type CalendarioAtendimentosRespose } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api/api";

const CALENDAR_PLUGINS = [dayGridPlugin, timeGridPlugin, interactionPlugin];

const HEADER_TOOLBAR = {
  left: "prev,next dayGridMonth,timeGridDay",
  center: "title",
  right: "today",
};

const BUTTON_TEXT = {
  today: "hoje",
  month: "mês",
  day: "dia",
};

function toCalendarEvent(atendimento: CalendarioAtendimentosRespose): EventInput {
  return {
    id: atendimento.id,
    title: `${atendimento.nomeAluno} - ${atendimento.nomeColaborador}`,
    start: atendimento.inicio,
    end: atendimento.fim,
    color: getAppointmentColor({ tipo: atendimento.tipo }).backgroundColor,
  };
}

export function AtendimentosCalendar() {
  const [calendarDate, setCalendarDate] = useState(() => new Date());
  const calendarRef = useRef<FullCalendar>(null);
  const navigate = useNavigate();

  const anoMes = new Date("2026-11-28").toISOString().slice(0, 7);
  const calendarioAtendimentos = useGetCalendarioAtendimentos({ anoMes });

  const calendarEvents = useMemo(
    () => (calendarioAtendimentos.data ?? []).map((atendimento) => toCalendarEvent(atendimento)),
    [calendarioAtendimentos.data],
  );

  const handleDateClick = (info: DateClickArg) => {
    setCalendarDate(info.date);
    calendarRef.current?.getApi().changeView("timeGridDay", info.date);
  };

  const handleEventClick = (info: EventClickArg) => {
    info.jsEvent.preventDefault();
    navigate(`/atendimentos/${info.event.id}`);
  };

  const handleDatesSet = (info: DatesSetArg) => {
    const next = info.view.currentStart.getTime();
    const current = calendarDate.getTime();
    if (next !== current) {
      setCalendarDate(info.view.currentStart);
    }
  };

  if (calendarioAtendimentos.isError) {
    return (
      <ErrorCard
        title="Não foi possível carregar os atendimentos"
        description={getFriendlyErrorMessage(calendarioAtendimentos.error)}
      />
    );
  }

  if (calendarioAtendimentos.isLoading) {
    return (
      <div className="card border border-base-300 bg-base-100 shadow-sm">
        <div className="card-body">
          <p className="text-sm text-base-content/60">
            Carregando atendimentos...
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="card border border-base-300 rounded-2xl bg-base-100 shadow-sm">
      <div className="card-body">
        <div className="mb-4 flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <h2 className="card-title text-lg font-semibold">Calendário de Atendimentos</h2>
            <p className="text-sm text-base-content/60">
              Visualize os atendimentos agendados e clique em um para ver mais
              detalhes.
            </p>
          </div>
        </div>


        <div className="appointments-calendar rounded-2xl border border-base-300 bg-base-100 p-3">
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
      </div>
    </div>
  );
}
