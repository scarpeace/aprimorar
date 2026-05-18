import { ErrorCard } from "@/components/ui/error-card";
import { Button } from "@/components/ui/button";
import { useGetAppointments } from "@/kubb";
import type { AppointmentResponseDTO } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
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

const CONTENT_COLORS: Record<string, { backgroundColor: string; borderColor: string }> = {
  AULA: { backgroundColor: "#3b82f6", borderColor: "#2563eb" },
  MENTORIA: { backgroundColor: "#10b981", borderColor: "#059669" },
  TERAPIA: { backgroundColor: "#f59e0b", borderColor: "#d97706" },
  ORIENTACAO_VOCACIONAL: { backgroundColor: "#8b5cf6", borderColor: "#7c3aed" },
  ENEM: { backgroundColor: "#ec4899", borderColor: "#db2777" },
  PAS: { backgroundColor: "#06b6d4", borderColor: "#0891b2" },
  OUTRO: { backgroundColor: "#6b7280", borderColor: "#4b5563" },
};

function getMonthRange(date: Date) {
  return {
    startDate: new Date(date.getFullYear(), date.getMonth(), 1),
    endDate: new Date(date.getFullYear(), date.getMonth() + 1, 1),
  };
}

function getAppointmentColor(appointment: AppointmentResponseDTO) {
  return CONTENT_COLORS[appointment.content] ?? CONTENT_COLORS.OUTRO;
}

function toCalendarEvent(appointment: AppointmentResponseDTO): EventInput {
  const color = getAppointmentColor(appointment);
  const contentLabel = EventContentLabels[appointment.content] ?? appointment.content;

  return {
    id: appointment.id,
    title: `${appointment.studentName} • ${contentLabel}`,
    start: appointment.startDate,
    end: appointment.endDate,
    backgroundColor: color.backgroundColor,
    borderColor: color.borderColor,
    textColor: "#fff",
    extendedProps: {
      content: appointment.content,
      employeeName: appointment.employeeName,
      studentName: appointment.studentName,
    },
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
