import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { AtendimentosCalendar } from "@/features/atendimentos/components/calendario/AtendimentosCalendar";

export default function DashboardPage() {
  return (
    <section className="space-y-6">
      <Card>
        <CardHeader>
          <div>
            <p className="text-sm font-semibold uppercase text-success">Home</p>
            <CardTitle>Visão geral da operação</CardTitle>
            <p className="mt-2 text-sm text-base-content/65">
              Resumo da operação e calendário de atendimentos.
            </p>
          </div>
        </CardHeader>
      </Card>

      <AtendimentosCalendar />
    </section>
  );
}
