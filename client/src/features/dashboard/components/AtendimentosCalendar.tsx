import { useMemo, useRef, useState } from "react";
import { Plus } from "lucide-react";
import { useNavigate } from "react-router-dom";
import type { DatesSetArg, EventClickArg, EventInput } from "@fullcalendar/core";
import ptBrLocale from "@fullcalendar/core/locales/pt-br";
import dayGridPlugin from "@fullcalendar/daygrid";
import type { DateClickArg } from "@fullcalendar/interaction";
import interactionPlugin from "@fullcalendar/interaction";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";

import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { getAppointmentColor } from "@/features/atendimentos/lib/appointment-content-colors";
import { useGetAtendimentos } from "@/kubb";
import type { AtendimentoResponseDTO } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { getMonthRange } from "@/lib/utils/date-utils";
import { AtendimentoContentLegend } from "./AtendimentoContentLegend";

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

function toCalendarEvent(atendimento: AtendimentoResponseDTO): EventInput {
  return {
    id: atendimento.id,
    title: atendimento.alunoNome,
    start: atendimento.inicio,
    end: atendimento.fim,
    color: getAppointmentColor(atendimento).backgroundColor,
  };
}

type AtendimentosCalendarProps = {
  onCreateAppointment: () => void;
};

export function AtendimentosCalendar({ onCreateAppointment }: Readonly<AtendimentosCalendarProps>) {
  const [calendarDate, setCalendarDate] = useState(() => new Date());
  const calendarRef = useRef<FullCalendar>(null);
  const navigate = useNavigate();

  const calendarRange = getMonthRange(calendarDate);

  const eventsQuery = useGetAtendimentos({
    inicio: calendarRange.startDate.toISOString(),
    fim: calendarRange.endDate.toISOString(),
    size: 500,
    sort: ["inicio,asc"],
  });

  const calendarEvents = useMemo(
    () => (eventsQuery.data?.content ?? []).map(toCalendarEvent),
    [eventsQuery.data?.content],
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

  if (eventsQuery.isError) {
    return (
      <ErrorCard
        title="Não foi possível carregar os atendimentos"
        description={getFriendlyErrorMessage(eventsQuery.error)}
      />
    );
  }

  if (eventsQuery.isLoading) {
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
          <Button onClick={onCreateAppointment} variant="success">
            <Plus className="mr-2 h-4 w-4" />
            Novo atendimento
          </Button>
        </div>

        <AtendimentoContentLegend
          startDate={calendarRange.startDate}
          endDate={calendarRange.endDate}
        />

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
