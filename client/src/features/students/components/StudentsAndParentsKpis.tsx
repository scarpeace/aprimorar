import { KpiCard } from "@/components/ui/kpi-card";
import { UserCheck, UserCircle, CheckCircle, GraduationCap, Handshake } from "lucide-react";

type StudentsAndParentsKpisProps = {
  activeStudentsCount: number;
  activeParentsCount: number;
  totalStudentsCount: number;
  totalParentsCount: number;
};

export function StudentsAndParentsKpis({ activeStudentsCount, activeParentsCount, totalStudentsCount, totalParentsCount }: StudentsAndParentsKpisProps) {
  return (
    <section className="rounded-2xl mb-3 bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
      <div className="flex flex-col gap-3">
        <div>
          <h3 className="text-2xl font-bold text-base-content">Indicadores de Alunos e Responsáveis</h3>
          <p className="text-sm text-base-content/60">
            Visão geral dos alunos e responsáveis ativos e do total cadastrado desde o inicio.
          </p>
        </div>

        <div className="flex gap-6">
          <KpiCard
            label="Alunos Ativos (Mockado)"
            value={activeStudentsCount}
            Icon={GraduationCap}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />

          <KpiCard
            label="Alunos Totais (Mockado)"
            value={totalStudentsCount}
            Icon={GraduationCap}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />

          <KpiCard
            label="Responsáveis Ativos (Mockado)"
            value={activeParentsCount}
            Icon={Handshake}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />

          <KpiCard
            label="Responsáveis Totais (Mockado)"
            value={totalParentsCount}
            Icon={Handshake}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />
        </div>
      </div>
    </section>

  )
}
