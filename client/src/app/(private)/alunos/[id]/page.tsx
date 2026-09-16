import { AlunoAtendimentosTable } from "@/features/alunos/components/atendimentos/AlunoAtendimentosTable";
import { AlunoCalendar } from "@/features/alunos/components/atendimentos/AlunoCalendar";
import { AlunoCobrancasHistory } from "@/features/alunos/components/cobrancas/AlunoCobrancasHistory";
import { AlunoProfile } from "@/features/alunos/components/AlunoProfile";

export default async function AlunoPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-6 lg:flex-row lg:items-stretch">
        <div className="min-w-0 lg:flex-2">
          <AlunoProfile alunoId={id} />
        </div>

        <div className="min-w-0 lg:flex-1">
          <AlunoCobrancasHistory alunoId={id} />
        </div>
      </div>

      <AlunoCalendar alunoId={id} />
      <AlunoAtendimentosTable alunoId={id} />
    </section>
  );
}
