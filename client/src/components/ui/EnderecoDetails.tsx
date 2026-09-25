import type { EnderecoResponse } from "@/lib/api/generated/types/EnderecoResponse";
import { DetailField } from "@/components/ui/DetailField";
import { formatZip } from "@/lib/utils/formatter";

type EnderecoDetailsProps = {
  endereco: EnderecoResponse;
};

export function EnderecoDetails({ endereco }: Readonly<EnderecoDetailsProps>) {
  return (
    <div className="space-y-4">
      <div className="grid gap-5 sm:grid-cols-2">
        <DetailField label="Rua" value={endereco.rua} />
        <DetailField label="Número" value={endereco.numero} />
        <DetailField label="Complemento" value={endereco.complemento} />
        <DetailField label="Bairro" value={endereco.bairro} />
        <DetailField label="Cidade" value={endereco.cidade} />
        <DetailField label="UF" value={endereco.estado} />
        <DetailField label="CEP" value={formatZip(endereco.cep)} />
      </div>
    </div>
  );
}
