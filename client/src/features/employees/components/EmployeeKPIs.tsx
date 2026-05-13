import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { Calendar, CircleDollarSign, Clock3 } from "lucide-react";

interface EmployeeKPIsProps {
  totalEvents?: number;
  totalPaid?: number;
  totalUnpaid?: number;
}

export function EmployeeKPIs({ totalEvents, totalPaid, totalUnpaid }: EmployeeKPIsProps) {
  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
      <KpiCard
        label="Total de eventos"
        value={totalEvents}
        Icon={Calendar}
      />

      <KpiCard
        label="Total pago"
        value={<span className="text-success">{brl.format(totalPaid ?? 0)}</span>}
        Icon={CircleDollarSign}
        className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
      />

      <KpiCard
        label="Total pendente"
        value={<span className="text-warning">{brl.format(totalUnpaid ?? 0)}</span>}
        Icon={Clock3}
        className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
      />
    </div>
  );
}
