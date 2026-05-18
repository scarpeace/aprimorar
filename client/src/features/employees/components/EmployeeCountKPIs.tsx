import { KpiCard } from "@/components/ui/kpi-card";
import { UserCheck, Users } from "lucide-react";

type EmployeeCountKPIsProps = {
  activeEmployees?: number;
  totalEmployees?: number;
};

export function EmployeeCountKPIs({
  activeEmployees,
  totalEmployees,
}: Readonly<EmployeeCountKPIsProps>) {
  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
      <KpiCard
        label="Colaboradores ativos"
        value={activeEmployees ?? 0}
        Icon={UserCheck}
        className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
      />

      <KpiCard
        label="Total de colaboradores"
        value={totalEmployees ?? 0}
        Icon={Users}
      />
    </div>
  );
}
