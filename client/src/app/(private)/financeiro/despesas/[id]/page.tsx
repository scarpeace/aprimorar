import { DespesaProfile } from "@/features/despesas/components/DespesaProfile";

export default async function DespesaPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return <DespesaProfile despesaId={id} />;
}
