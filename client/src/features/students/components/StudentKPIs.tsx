import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { Calendar, CircleDollarSign, Clock3 } from "lucide-react";

type StudentKPIsProps = {
  totalEvents?: number;
  totalCharged?: number;
  totalPending?: number;
  showTotalEvents?: boolean;
};

export function StudentKPIs({
  totalEvents,
  totalCharged,
  totalPending,
  showTotalEvents = true,
}: Readonly<StudentKPIsProps>) {
  return (
    <div className={`grid grid-cols-1 gap-4 ${showTotalEvents ? "md:grid-cols-3" : "md:grid-cols-2"}`}>
      {showTotalEvents ? (
        <KpiCard
          label="Total de eventos"
          value={totalEvents}
          Icon={Calendar}
        />
      ) : null}

      <KpiCard
        label="Total pago"
        value={<span className="text-success">{brl.format(totalCharged ?? 0)}</span>}
        Icon={CircleDollarSign}
        className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
      />

      <KpiCard
        label="Total pendente"
        value={<span className="text-warning">{brl.format(totalPending ?? 0)}</span>}
        Icon={Clock3}
        className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
      />
    </div>
  );
}
