import type { AlunoDetailResponseDTO } from "@/lib/api/generated/types/AlunoDetailResponseDTO";
import { Collapse } from "@/components/ui/Collapse";
import { DetailField } from "@/components/ui/DetailField";
import { EnderecoDetails } from "@/components/ui/EnderecoDetails";
import { formatCpf, formatDate, formatPhone } from "@/lib/utils/formatter";

type AlunoDetailsProps = {
  aluno: AlunoDetailResponseDTO;
};

export function AlunoDetails({ aluno }: Readonly<AlunoDetailsProps>) {
  return (
    <div className="mt-3 space-y-6">
        <div className="space-y-4">
          <h3 className="text-sm font-bold uppercase tracking-wider text-base-content/60">Dados pessoais</h3>

          <div className="grid gap-5 sm:grid-cols-2">
            <DetailField label="CPF" value={formatCpf(aluno.cpf)} />
            <DetailField label="Telefone" value={formatPhone(aluno.telefone)} />
            <DetailField label="E-mail" value={aluno.email} />
            <DetailField label="Data de nascimento" value={formatDate(aluno.dataNascimento)} />
            <DetailField label="Escola" value={aluno.escola} />
            <DetailField label="Criado em" value={formatDate(aluno.createdAt)} />
          </div>
        </div>

        <Collapse title="Responsável">
          <div className="grid gap-5 sm:grid-cols-2">
            <DetailField label="Nome" value={aluno.responsavel.nome} />
            <DetailField label="CPF" value={formatCpf(aluno.responsavel.cpf)} />
            <DetailField label="Telefone" value={formatPhone(aluno.responsavel.telefone)} />
            <DetailField label="E-mail" value={aluno.responsavel.email} />
          </div>
        </Collapse>

      <Collapse title="Endereço">
        <EnderecoDetails endereco={aluno.endereco} />
      </Collapse>
    </div>
  );
}
