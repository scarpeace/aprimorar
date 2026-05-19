import { ErrorCard } from "@/components/ui/error-card";
import { Button } from "@/components/ui/button";
import { useGetAppointments } from "@/kubb";
import type { AppointmentResponseDTO } from "@/kubb";
import { getAppointmentColor } from "@/features/appointments/lib/appointment-content-colors";
import { getMonthRange } from "@/lib/utils/date-utils";
import type { EventClickArg, EventInput } from "@fullcalendar/core";
import ptBrLocale from "@fullcalendar/core/locales/pt-br";
import dayGridPlugin from "@fullcalendar/daygrid";
import type { DateClickArg } from "@fullcalendar/interaction";
import interactionPlugin from "@fullcalendar/interaction";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import { useRef, useState } from "react";
import { Plus } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { AppointmentContentLegend } from "./AppointmentContentLegend";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

function toCalendarEvent(appointment: AppointmentResponseDTO): EventInput {
  const color = getAppointmentColor(appointment);

  return {
    id: appointment.id,
    title: appointment.studentName,
    start: appointment.startDate,
    end: appointment.endDate,
    color: color.backgroundColor,
  };
}

type AppointmentsCalendarProps = {
  onCreateAppointment: () => void;
};

export function AppointmentsCalendar({ onCreateAppointment }: Readonly<AppointmentsCalendarProps>) {
  const [calendarDate, setCalendarDate] = useState(() => new Date());
  const calendarRef = useRef<FullCalendar>(null);
  const navigate = useNavigate();

  const calendarRange = getMonthRange(calendarDate);
  const eventsQuery = useGetAppointments({
    startDate: calendarRange.startDate.toISOString(),
    endDate: calendarRange.endDate.toISOString(),
    size: 500,
    sort: ["startDate,asc"],
  });

  const calendarEvents = (eventsQuery.data?.content ?? []).map(toCalendarEvent);
  const totalAppointments = eventsQuery.data?.totalElements ?? 0;

  const handleDateClick = (info: DateClickArg) => {
    setCalendarDate(info.date);
    calendarRef.current?.getApi().changeView("timeGridDay", info.date);
  };

  const handleEventClick = (info: EventClickArg) => {
    info.jsEvent.preventDefault();
    navigate(`/appointments/${info.event.id}`);
  };

  return (
    <div className="card border border-base-300 bg-base-100 shadow-sm">
      <div className="card-body">
        <div className="mb-4 flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <h2 className="card-title text-lg font-semibold">
              Calendário de Atendimentos
            </h2>
            <p className="text-sm text-base-content/60">
              Visualização mensal e diária dos atendimentos.
            </p>
          </div>

          <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
            <div className="rounded-2xl border border-primary/15 bg-primary/5 px-4 py-3 shadow-sm">
              <p className="text-[10px] font-bold uppercase tracking-[0.18em] text-base-content/45">
                Atendimentos no período
              </p>
              <p className="mt-1 text-2xl font-extrabold text-base-content">
                {eventsQuery.isPending ? "..." : totalAppointments}
              </p>
            </div>

            <Button onClick={onCreateAppointment} variant="success">
              <Plus className="mr-2 h-4 w-4" />
              Novo atendimento
            </Button>
          </div>
        </div>

        {eventsQuery.isError ? (
          <ErrorCard
            title="Não foi possível carregar os atendimentos"
            description={getFriendlyErrorMessage(eventsQuery.error)}
          />
        ) : (
          <>
            <AppointmentContentLegend />

            <FullCalendar
              ref={calendarRef}
              locale={ptBrLocale}
              plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
              initialView="dayGridMonth"
              initialDate={calendarDate}
              headerToolbar={{
                left: "prev,next dayGridMonth,timeGridDay",
                center: "title",
                right: "today",
              }}
              buttonText={{
                today: "hoje",
                month: "mês",
                day: "dia",
              }}
              events={calendarEvents}
              eventDisplay="list-item"
              eventClick={handleEventClick}
              dateClick={handleDateClick}
              datesSet={(info) => setCalendarDate(info.view.currentStart)}
              allDaySlot={false}
              height="auto"
              dayMaxEvents={3}
              moreLinkText="mais"
              slotMinTime="07:00:00"
              slotMaxTime="21:00:00"
            />

            {eventsQuery.isPending ? (
              <p className="mt-3 text-sm text-base-content/60">Carregando atendimentos...</p>
            ) : null}

          </>
        )}
      </div>
    </div>
  );
}
