import { ColaboradorAtendimentos } from "@/features/colaboradores/components/ColaboradorAtendimentos";
import { ColaboradorInfo } from "@/features/colaboradores/components/ColaboradorInfo";

export default async function ColaboradorPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return (
    <section className="space-y-6">
      <ColaboradorInfo colaboradorId={id} />
      <ColaboradorAtendimentos colaboradorId={id} />
    </section>
  );
}
