"use client";

import ptBrLocale from "@fullcalendar/core/locales/pt-br";
import type { DatesSetArg } from "@fullcalendar/core";
import interactionPlugin from "@fullcalendar/interaction";
import listPlugin from "@fullcalendar/list";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import { useState } from "react";
import type { SharedCalendarProps } from "@/features/atendimentos/components/calendarios/calendar-shared";
import { Button } from "@/components/ui/Button";

const CALENDAR_PLUGINS = [listPlugin, timeGridPlugin, interactionPlugin];

const MOBILE_VIEW_OPTIONS = [
  { value: "timeGridDay", label: "Dia" },
  { value: "listWeek", label: "Semana" },
  { value: "listMonth", label: "Mês" },
] as const;

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

export function CalendarMobile({ calendarRef, calendarDate, events, onDatesSet, onEventClick, onOpenDay }: SharedCalendarProps) {
  const [calendarTitle, setCalendarTitle] = useState("");
  const [currentView, setCurrentView] = useState("listWeek");

  function handleDatesSet(info: DatesSetArg) {
    setCalendarTitle(info.view.title);
    setCurrentView(info.view.type);
    onDatesSet(info);
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

  return (
    <>
      <div className="mb-3 space-y-2 border-b border-base-300 pb-3">
        <div className="flex items-center gap-1.5">
          <Button
            type="button"
            variant="ghost"
            size="xs"
            className="btn-square"
            aria-label="Período anterior"
            onClick={handlePrevious}
          >
            ‹
          </Button>

          <div className="min-w-0 flex-1 text-center">
            <p className="truncate text-xs font-semibold text-base-content">{calendarTitle}</p>
          </div>

          <Button type="button" variant="ghost" size="xs" className="px-2" onClick={handleToday}>
            Hoje
          </Button>

          <Button
            type="button"
            variant="ghost"
            size="xs"
            className="btn-square"
            aria-label="Próximo período"
            onClick={handleNext}
          >
            ›
          </Button>
        </div>

        <div className="join flex w-full">
          {MOBILE_VIEW_OPTIONS.map((option) => {
            const isActive = currentView === option.value;

            return (
              <button
                key={option.value}
                type="button"
                className={`join-item btn btn-xs min-w-0 flex-1 ${isActive ? "btn-primary" : "btn-ghost"}`}
                onClick={() => handleChangeView(option.value)}
              >
                <span className="truncate">{option.label}</span>
              </button>
            );
          })}
        </div>
      </div>

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
        datesSet={handleDatesSet}
        height="auto"
        navLinks
        navLinkDayClick={(date) => onOpenDay(date)}
        noEventsContent={() => "Nenhum atendimento"}
        allDaySlot={false}
        slotMinTime="07:00:00"
        slotMaxTime="21:00:00"
      />
    </>
  );
}
