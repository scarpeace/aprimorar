import { CobrancaLoteProfile } from "@/features/cobrancas/components/CobrancaLoteProfile";

export default async function CobrancaLotePage({
  params,
}: Readonly<{
  params: Promise<{ loteId: string }>;
}>) {
  const { loteId } = await params;

  return <CobrancaLoteProfile loteId={loteId} />;
}
