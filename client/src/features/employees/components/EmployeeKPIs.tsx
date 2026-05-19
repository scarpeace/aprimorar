import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { CircleDollarSign, Clock3 } from "lucide-react";

interface EmployeeKPIsProps {
  totalPaid?: number;
  totalUnpaid?: number;
}

export function EmployeeKPIs({
  totalPaid,
  totalUnpaid,
}: EmployeeKPIsProps) {
  return (
    <div className={`flex flex-col rounded-xl p-3 gap-4 border-2 border-base-300`}>
      <h1 className="text-2xl font-bold text-base-content">Colaboradores</h1>

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
