import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { CircleDollarSign, Clock3 } from "lucide-react";

type StudentKPIsProps = {
  totalCharged?: number;
  totalPending?: number;
};

export function StudentKPIs({
  totalCharged,
  totalPending,
}: Readonly<StudentKPIsProps>) {
  return (
    <div className={`flex flex-col rounded-xl p-3 gap-4 border-2 border-base-300`}>
      <h1 className="text-2xl font-bold text-base-content">Alunos</h1>

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
