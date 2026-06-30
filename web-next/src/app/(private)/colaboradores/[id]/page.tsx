import { ColaboradorDetails } from "@/components/colaboradores/ColaboradorDetails";

export default async function ColaboradorDetailsPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return <ColaboradorDetails colaboradorId={id} />;
}
