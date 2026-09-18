import { RepasseLoteDetails } from "@/features/repasses/components/RepasseLoteDetails";

export default async function RepasseLotePage({
  params,
}: Readonly<{
  params: Promise<{ loteId: string }>;
}>) {
  const { loteId } = await params;

  return <RepasseLoteDetails loteId={loteId} />;
}
