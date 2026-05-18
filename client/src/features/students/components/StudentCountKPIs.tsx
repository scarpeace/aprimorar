import { KpiCard } from "@/components/ui/kpi-card";
import { UserCheck, Users } from "lucide-react";

type StudentCountKPIsProps = {
  activeStudents?: number;
  totalStudents?: number;
};

export function StudentCountKPIs({
  activeStudents,
  totalStudents,
}: Readonly<StudentCountKPIsProps>) {
  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
      <KpiCard
        label="Alunos ativos"
        value={activeStudents ?? 0}
        Icon={UserCheck}
        className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
      />

      <KpiCard
        label="Total de alunos"
        value={totalStudents ?? 0}
        Icon={Users}
      />
    </div>
  );
}
