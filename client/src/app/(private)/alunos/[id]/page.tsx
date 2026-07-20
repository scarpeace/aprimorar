import { AlunoAtendimentos } from "@/components/alunos/AlunoAtendimentos";
import { AlunoInfo } from "@/components/alunos/AlunoInfo";

export default async function AlunoPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return (
    <section className="space-y-6">
      <AlunoInfo alunoId={id} />
      <AlunoAtendimentos alunoId={id} />
    </section>
  );
}
