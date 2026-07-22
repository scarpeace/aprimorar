"use client";

import type { EventContentArg } from "@fullcalendar/core";
import type { DateClickArg } from "@fullcalendar/interaction";
import ptBrLocale from "@fullcalendar/core/locales/pt-br";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import type { SharedCalendarProps } from "@/components/dashboard/calendar-shared";

const CALENDAR_PLUGINS = [dayGridPlugin, timeGridPlugin, interactionPlugin];

const VIEWS = {
  dayGridMonth: {
    titleFormat: { month: "long", year: "numeric" } as const,
  },
  timeGridWeek: {
    titleFormat: { month: "short", day: "numeric", year: "numeric" } as const,
  },
  timeGridDay: {
    titleFormat: { month: "short", day: "numeric", year: "numeric" } as const,
  },
};

function renderEventContent(info: EventContentArg) {
  const { nomeAluno, nomeColaborador } = info.event.extendedProps as {
    nomeAluno?: string;
    nomeColaborador?: string;
  };
  const color = info.event.backgroundColor || info.event.borderColor;

  if (info.view.type !== "dayGridMonth") {
    return (
      <span className="truncate text-xs font-semibold">
        {info.timeText} {info.event.title}
      </span>
    );
  }

  return (
    <div
      className="flex min-w-0 gap-1.5 rounded px-1 py-0.5 leading-tight"
      style={{ backgroundColor: color ? `${color}18` : undefined }}
      title={info.event.title}
    >
      <span
        className="mt-1 h-2 w-2 shrink-0 rounded-full"
        style={{ backgroundColor: color }}
      />
      <div className="min-w-0">
        <p className="truncate text-xs font-semibold">
          <span>{info.timeText}</span> {nomeAluno ?? info.event.title}
        </p>
        {nomeColaborador ? (
          <p className="truncate text-[11px] text-base-content/60">
            {nomeColaborador}
          </p>
        ) : null}
      </div>
    </div>
  );
}

export function CalendarDesktop({
  calendarRef,
  calendarDate,
  events,
  onDatesSet,
  onEventClick,
  onOpenDay,
}: SharedCalendarProps) {
  function handleDateClick(info: DateClickArg) {
    onOpenDay(info.date);
  }

  return (
    <FullCalendar
      ref={calendarRef}
      locale={ptBrLocale}
      plugins={CALENDAR_PLUGINS}
      initialView="dayGridMonth"
      initialDate={calendarDate}
      headerToolbar={false}
      views={VIEWS}
      events={events}
      eventDisplay="list-item"
      eventContent={renderEventContent}
      eventTimeFormat={{ hour: "2-digit", minute: "2-digit", hour12: false }}
      eventClick={onEventClick}
      dateClick={handleDateClick}
      datesSet={onDatesSet}
      allDaySlot={false}
      height="auto"
      dayMaxEvents={true}
      fixedWeekCount={false}
      moreLinkText="mais"
      slotMinTime="07:00:00"
      slotMaxTime="21:00:00"
    />
  );
}
