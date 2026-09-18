import { CobrancaLoteDetails } from "@/features/cobrancas/components/CobrancaLoteDetails";

export default async function CobrancaLotePage({
  params,
}: Readonly<{
  params: Promise<{ loteId: string }>;
}>) {
  const { loteId } = await params;

  return <CobrancaLoteDetails loteId={loteId} />;
}
