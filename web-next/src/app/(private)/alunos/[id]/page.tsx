import { AlunoDetails } from "@/components/alunos/AlunoDetails";

export default async function AlunoDetailsPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return <AlunoDetails alunoId={id} />;
}
