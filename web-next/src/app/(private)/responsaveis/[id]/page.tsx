import { ResponsavelDetails } from "@/components/responsaveis/ResponsavelDetails";

export default async function ResponsavelDetailsPage({
  params,
}: Readonly<{
  params: Promise<{ id: string }>;
}>) {
  const { id } = await params;

  return <ResponsavelDetails responsavelId={id} />;
}
