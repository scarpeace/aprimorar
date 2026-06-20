import { SummaryItem } from "@/components/summary-item.tsx";
import type { EnderecoResponseDTO } from "@/kubb";

type AddressSummarySectionProps = {
  address?: EnderecoResponseDTO;
};

export function AddressDetails({
  address,
}: Readonly<AddressSummarySectionProps>) {

  return (
    <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
      <SummaryItem className="md:col-span-2" label="Rua" value={address?.rua} />
      <SummaryItem label="Número" value={address?.numero ?? "N/A"} />
      <SummaryItem label="Complemento" value={address?.complemento ?? "N/A"} />
      <SummaryItem label="Bairro" value={address?.bairro} />
      <div className="flex gap-3 justify-between">
        <SummaryItem className="grow" label="Cidade" value={address?.cidade}/>
        <SummaryItem label="Estado" value={address?.estado}/>
      </div>
      <SummaryItem label="CEP" value={address?.cep} />
    </div>
  );
}
