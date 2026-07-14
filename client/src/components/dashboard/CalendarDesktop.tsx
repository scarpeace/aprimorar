"use client";

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
