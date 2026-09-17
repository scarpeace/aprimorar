import { AtendimentoDetails } from "@/features/atendimentos/components/AtendimentoDetails";

export default async function AtendimentoDetailsPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return (
    <section className="space-y-6">
      <AtendimentoDetails atendimentoId={id} />
    </section>
  );
}
