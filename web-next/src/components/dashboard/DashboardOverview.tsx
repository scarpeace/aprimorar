"use client";

import { useGetAlunosKpis } from "@/lib/api/generated/hooks/aluno/useGetAlunosKpis";
import { useGetRelatorioAtendimentos } from "@/lib/api/generated/hooks/atendimento/useGetRelatorioAtendimentos";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { MetricCard } from "@/components/ui/MetricCard";
import { PageLoading } from "@/components/ui/PageLoading";

function getAnoMesAtual() {
  const now = new Date();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  return `${now.getFullYear()}-${month}`;
}

function formatAnoMesLabel(anoMes: string) {
  const [year, month] = anoMes.split("-");
  const date = new Date(Number(year), Number(month) - 1, 1);
  return new Intl.DateTimeFormat("pt-BR", { month: "long", year: "numeric" }).format(date);
}

export function DashboardOverview() {
  const anoMes = getAnoMesAtual();
  const alunosKpis = useGetAlunosKpis();
  const relatorio = useGetRelatorioAtendimentos({ anoMes });

  if (alunosKpis.isLoading || relatorio.isLoading) {
    return <PageLoading message="Carregando dashboard..." />;
  }

  if (alunosKpis.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar os KPIs de alunos"
        description="O dashboard depende dos dados de alunos para montar a visão inicial."
        error={alunosKpis.error}
      />
    );
  }

  if (relatorio.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar o relatório de atendimentos"
        description="O resumo mensal não ficou disponível para o mês atual."
        error={relatorio.error}
      />
    );
  }

  if (!alunosKpis.data || !relatorio.data) {
    return (
      <EmptyCard
        title="Dashboard sem dados"
        description="Os serviços responderam sem conteúdo suficiente para montar a visão inicial."
      />
    );
  }

  return (
    <div className="space-y-6">
      <section className="grid gap-3 md:grid-cols-2 xl:grid-cols-4">
        <MetricCard label="Alunos cadastrados" value={alunosKpis.data.totalAlunos} />
        <MetricCard label="Alunos ativos" value={alunosKpis.data.totalAlunosAtivos} tone="success" />
        <MetricCard label="Atendimentos no mês" value={relatorio.data.totalAtendimentos} />
        <MetricCard label="Aulas no mês" value={relatorio.data.totalAulas} />
      </section>

      <section className="rounded-2xl border border-base-300 bg-base-100 p-6 shadow-sm">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Mês atual</p>
        <h2 className="mt-2 text-2xl font-bold text-base-content">{formatAnoMesLabel(anoMes)}</h2>
        <p className="mt-2 text-sm text-base-content/65">
          Resumo rápido dos tipos de atendimento já registrados no período.
        </p>

        <div className="mt-6 grid gap-3 md:grid-cols-2 xl:grid-cols-3">
          <MetricCard label="Mentorias" value={relatorio.data.totalMentoria} />
          <MetricCard label="Terapias" value={relatorio.data.totalTerapia} />
          <MetricCard label="Orientação vocacional" value={relatorio.data.totalOV} />
          <MetricCard label="ENEM" value={relatorio.data.totalENEM} />
          <MetricCard label="PAS" value={relatorio.data.totalPAS} />
          <MetricCard label="Outros" value={relatorio.data.totalOutros} />
        </div>
      </section>
    </div>
  );
}
