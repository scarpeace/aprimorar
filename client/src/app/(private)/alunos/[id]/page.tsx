
import { AlunoCalendar } from "@/features/alunos/components/atendimentos/AlunoCalendar";
import { AlunoProfile } from "@/features/alunos/components/AlunoProfile";

export default async function AlunoPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return (
    <section className="space-y-6">
      <AlunoProfile alunoId={id} />
      <AlunoCalendar alunoId={id} />
    </section>
  );
}
