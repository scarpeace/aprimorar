import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { Calendar, CircleDollarSign, Clock3 } from "lucide-react";
import { memo } from "react";

interface StudentKPIsProps {
  totalEvents?: number;
  totalCharged?: number;
  totalPending?: number;
}

export const StudentKPIs = memo(function StudentKPIs({
  totalEvents = 0,
  totalCharged = 0,
  totalPending = 0,
}: StudentKPIsProps) {

  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
      <KpiCard
        label="Total de eventos"
        value={totalEvents}
        Icon={Calendar}
      />

      <KpiCard
        label="Total pago"
        value={<span className="text-success">{brl.format(totalCharged)}</span>}
        Icon={CircleDollarSign}
        className="bg-gradient-to-br from-success/8 via-base-100 to-base-100"
      />

      <KpiCard
        label="Total pendente"
        value={<span className="text-warning">{brl.format(totalPending)}</span>}
        Icon={Clock3}
        className="bg-gradient-to-br from-warning/10 via-base-100 to-base-100"
      />
    </div>
  );
});
