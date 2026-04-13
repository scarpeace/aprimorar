import type { AddressResponseDTO } from "@/kubb";
import type { ReactNode } from "react";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";

type AddressSummarySectionProps = {
  address?: AddressResponseDTO;
};

export function AddressDetails({
  address,
}: Readonly<AddressSummarySectionProps>) {

  return (
       <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
      <SummaryItem className="md:col-span-2" label="Rua" value={address?.street} />
      <SummaryItem label="Complemento" value={address?.complement ?? "N/A"} />
      <SummaryItem label="Bairro" value={address?.district} />
      <div className="flex gap-3 justify-between">
        <SummaryItem className="grow" label="Cidade" value={address?.city}/>
        <SummaryItem label="Estado" value={address?.state}/>
      </div>
        <SummaryItem label="CEP" value={address?.zip} />
    </div>
  );
}
