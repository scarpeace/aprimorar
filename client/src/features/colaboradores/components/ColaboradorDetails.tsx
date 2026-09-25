import type { ColaboradorResponse } from "@/lib/api/generated/types/ColaboradorResponse";
import { Collapse } from "@/components/ui/Collapse";
import { DetailField } from "@/components/ui/DetailField";
import { EnderecoDetails } from "@/components/ui/EnderecoDetails";
import { formatCpf, formatDate, formatPhone } from "@/lib/utils/formatter";

type ColaboradorDetailsProps = {
  colaborador: ColaboradorResponse;
};

export function ColaboradorDetails({ colaborador }: Readonly<ColaboradorDetailsProps>) {
  return (
    <div className="mt-3 space-y-6">
        <div className="space-y-4">
          <h3 className="text-sm font-bold uppercase tracking-wider text-base-content/60">Dados pessoais</h3>

          <div className="grid gap-5 sm:grid-cols-2">
            <DetailField label="CPF" value={formatCpf(colaborador.cpf)} />
            <DetailField label="Telefone" value={formatPhone(colaborador.telefone)} />
            <DetailField label="E-mail" value={colaborador.email} />
            <DetailField label="Data de nascimento" value={formatDate(colaborador.dataNascimento)} />
            <DetailField label="PIX" value={colaborador.pix} />
            <DetailField label="Criado em" value={formatDate(colaborador.createdAt)} />
          </div>
        </div>

      <Collapse title="Endereço">
        <EnderecoDetails endereco={colaborador.endereco} />
      </Collapse>
    </div>
  );
}
