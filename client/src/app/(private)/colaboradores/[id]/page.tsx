import { ColaboradorDetails } from "@/components/colaboradores/ColaboradorDetails";

export default async function ColaboradorDetailsPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return (
    <section className="space-y-6">
      <div className="app-shell-card p-6 md:p-8">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Colaboradores</p>
        <h1 className="mt-2 text-3xl font-bold text-base-content">Detalhes do colaborador</h1>
        <p className="mt-3 max-w-2xl text-sm text-base-content/65">
          Consulte dados cadastrais, endereço e status do colaborador selecionado.
        </p>
      </div>

      <ColaboradorDetails colaboradorId={id} />
    </section>
  );
}
