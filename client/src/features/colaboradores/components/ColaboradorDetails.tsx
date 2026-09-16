
import type { ColaboradorDetailResponseDTO } from "@/lib/api/generated/types/ColaboradorDetailResponseDTO";
import { DetailField } from "@/components/ui/DetailField";
import { EnderecoDetails } from "@/components/ui/EnderecoDetails";
import { formatCpf, formatDate, formatPhone } from "@/lib/utils/formatter";

type ColaboradorDetailsProps = {
  colaborador: ColaboradorDetailResponseDTO;
};

export function ColaboradorDetails({ colaborador }: Readonly<ColaboradorDetailsProps>) {

  return (
      <div className="mt-3">
        <h2 className="text-lg font-bold text-base-content">Dados cadastrais</h2>

        <div className="mt-5 grid gap-6 lg:grid-cols-2 lg:gap-8">
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

          <div className="lg:border-l lg:border-base-300 lg:pl-8">
            <EnderecoDetails endereco={colaborador.endereco} />
          </div>
        </div>
      </div>
  );
}
