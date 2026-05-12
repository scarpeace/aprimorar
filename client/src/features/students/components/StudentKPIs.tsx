import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { Calendar, CircleDollarSign, Clock3 } from "lucide-react";
import { memo } from "react";

interface StudentKPIsProps {
  studentId: string;
}

export const StudentKPIs = memo(function StudentKPIs({ studentId }: StudentKPIsProps) {
  void studentId;

  // const studentSummaryQuery = useGetApp(studentId);
  const mockKpis = {
    totalEvents: 47,
    totalPaid: 2520,
    totalPending: 3120,
  };

  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
      <KpiCard
        label="Total de eventos"
        value={mockKpis.totalEvents}
        Icon={Calendar}
      />

      <KpiCard
        label="Total pago"
        value={<span className="text-success">{brl.format(mockKpis.totalPaid)}</span>}
        Icon={CircleDollarSign}
        className="bg-gradient-to-br from-success/8 via-base-100 to-base-100"
      />

      <KpiCard
        label="Total pendente"
        value={<span className="text-warning">{brl.format(mockKpis.totalPending)}</span>}
        Icon={Clock3}
        className="bg-gradient-to-br from-warning/10 via-base-100 to-base-100"
      />
    </div>
  );
});
