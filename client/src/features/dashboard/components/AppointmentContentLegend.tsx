import { EventContentLabels } from "@/features/appointments/lib/eventContentLables.ts";
import { APPOINTMENT_CONTENT_COLORS } from "@/features/appointments/lib/appointment-content-colors";

export function AppointmentContentLegend() {
  return (
    <div className="mb-4 rounded-2xl border border-base-300 bg-base-200/40 px-4 py-3">
      <p className="mb-2 text-xs font-bold uppercase tracking-[0.16em] text-base-content/50">
        Legenda dos tipos de atendimento
      </p>
      <div className="flex flex-wrap gap-3">
        {Object.entries(APPOINTMENT_CONTENT_COLORS).map(([content, color]) => (
          <div key={content} className="flex items-center gap-2 text-sm font-medium text-base-content/75">
            <span
              className="h-2.5 w-2.5 rounded-full"
              style={{ backgroundColor: color.backgroundColor }}
            />
            <span>{EventContentLabels[content] ?? content}</span>
          </div>
        ))}
      </div>
    </div>
  );
}
