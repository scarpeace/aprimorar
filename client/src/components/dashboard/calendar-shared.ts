import type {
  DatesSetArg,
  EventClickArg,
  EventInput,
} from "@fullcalendar/core";
import type FullCalendar from "@fullcalendar/react";
import type { RefObject } from "react";

export type SharedCalendarProps = {
  calendarRef: RefObject<FullCalendar | null>;
  calendarDate: Date;
  events: EventInput[];
  onDatesSet: (info: DatesSetArg) => void;
  onEventClick: (info: EventClickArg) => void;
  onOpenDay: (date: Date) => void;
};
