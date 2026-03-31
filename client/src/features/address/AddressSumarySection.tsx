import type { AddressResponseDTO } from "@/kubb";
import type { ReactNode } from "react";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";

type AddressSummarySectionProps = {
  address?: AddressResponseDTO;
};

export function AddressSummarySection({
  address,
}: Readonly<AddressSummarySectionProps>) {
  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Rua", value: address?.street },
    { label: "Número", value: address?.number },
    { label: "Complemento", value: address?.complement ?? "N/A" },
    { label: "Bairro", value: address?.district },
    { label: "Cidade", value: address?.city },
    { label: "Estado", value: address?.state },
    { label: "CEP", value: address?.zip },
  ];

  return (
    <SectionCard
      title="Endereço"
      description="Dados de endereço"
    >
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {summaryItems.map((item) => (
          <SummaryItem key={item.label} label={item.label} value={item.value} />
        ))}
      </div>
    </SectionCard>
  );
}
