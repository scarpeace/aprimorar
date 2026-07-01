import { ResponsavelDetails } from "@/components/responsaveis/ResponsavelDetails";

export default async function ResponsavelDetailsPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return (
    <section className="space-y-6">
      <div className="app-shell-card p-6 md:p-8">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Responsáveis</p>
        <h1 className="mt-2 text-3xl font-bold text-base-content">Detalhes do responsável</h1>
        <p className="mt-3 max-w-2xl text-sm text-base-content/65">
          Consulte e atualize os dados cadastrais do responsável selecionado.
        </p>
      </div>

      <ResponsavelDetails responsavelId={id} />
    </section>
  );
}
