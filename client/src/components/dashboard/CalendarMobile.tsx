"use client";

import ptBrLocale from "@fullcalendar/core/locales/pt-br";
import interactionPlugin from "@fullcalendar/interaction";
import listPlugin from "@fullcalendar/list";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import type { SharedCalendarProps } from "@/components/dashboard/calendar-shared";

const CALENDAR_PLUGINS = [listPlugin, timeGridPlugin, interactionPlugin];

const VIEWS = {
  listWeek: {
    titleFormat: { month: "short", day: "numeric", year: "numeric" } as const,
  },
  listMonth: {
    titleFormat: { month: "long", year: "numeric" } as const,
  },
  timeGridDay: {
    titleFormat: { month: "short", day: "numeric", year: "numeric" } as const,
  },
};

export function CalendarMobile({
  calendarRef,
  calendarDate,
  events,
  onDatesSet,
  onEventClick,
  onOpenDay,
}: SharedCalendarProps) {
  return (
    <FullCalendar
      ref={calendarRef}
      locale={ptBrLocale}
      plugins={CALENDAR_PLUGINS}
      initialView="listWeek"
      initialDate={calendarDate}
      headerToolbar={false}
      views={VIEWS}
      events={events}
      eventClick={onEventClick}
      datesSet={onDatesSet}
      height="auto"
      navLinks
      navLinkDayClick={(date) => onOpenDay(date)}
      noEventsContent={() => "Nenhum atendimento"}
      allDaySlot={false}
      slotMinTime="07:00:00"
      slotMaxTime="21:00:00"
    />
  );
}
