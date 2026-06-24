import { Button } from "@/components/Ui/Button.tsx";
import { Collapse } from "@/components/Ui/Collapse.tsx";
import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { LoadingCard } from "@/components/Ui/LoadingCard.tsx";
import { SummaryItem } from "@/components/Ui/SummaryItem.tsx";
import {
  formatCpf,
  formatPhone,
} from "@/utils/formatter.ts";
import { formatDateShortYear } from "@/utils/date-utils.ts";
import { useGetAlunoById, useGetResponsavelById } from "@/kubb";
import { Edit, Handshake, User } from "lucide-react";
import { ArchiveAlunoButton } from "./ArchiveAlunoButton.tsx";
import { DeleteAlunoButton } from "./DeleteAlunoButton.tsx";
import { AddressDetails } from "@/components/Ui/AddressDetails.tsx";

type AlunoInfoSectionProps = {
  alunoId: string;
  onEdit: () => void;
};

export const AlunoInfoSection = ({ alunoId, onEdit }: Readonly<AlunoInfoSectionProps>) => {
  const alunoQuery = useGetAlunoById(alunoId);
  const responsavelQuery = useGetResponsavelById(alunoQuery.data?.responsavelId || "");

  if (alunoQuery.error || responsavelQuery.error) {
    return <ErrorCard title="Erro ao carregar detalhes do aluno" error={alunoQuery.error || responsavelQuery.error} />;
  }

  if (alunoQuery.isPending || !alunoQuery.data || responsavelQuery.isPending || !responsavelQuery.data) {
    return <LoadingCard title="Carregando detalhes do aluno" />;
  }

  const isActive = alunoQuery.data.active ?? true;

  return (
    <section className="card rounded-xl border border-base-300 bg-base-100 shadow-sm">
      <div className="card-body">
        <div className="card-actions justify-between">
          <div className="card-title text-2xl font-bold text-base-content">
            {alunoQuery.data.nome}
            <span className={`text-xs md:text-md badge ${isActive ? "badge-success" : "badge-ghost"} badge-md gap-1`}>
              {isActive ? "Ativo" : "Arquivado"}
            </span>
            <span className="text-xs md:text-md badge badge-outline badge-md">{alunoQuery.data.idade} anos</span>
            <span className="text-xs md:text-md badge badge-outline badge-md">{alunoQuery.data.escola}</span>
          </div>

          <div className="flex gap-2 items-center justify-start">
            <Button onClick={onEdit} variant="primary" size="sm" className="sm:btn-md">
              <Edit className="h-4 w-4 mr-1 sm:mr-2" />
              Editar
            </Button>
            <ArchiveAlunoButton className="btn-sm md:btn-md" alunoId={alunoQuery.data.id} active={isActive} />
            <DeleteAlunoButton className="btn-sm md:btn-md" alunoId={alunoQuery.data.id} />
          </div>
        </div>

        <div className="flex flex-col gap-8">
          <div className="grid gap-8 md:grid-cols-2">
            <div className="animate-[fade-up_500ms_ease-out_both]">
              <h4 className="flex items-center gap-2 text-sm font-bold uppercase tracking-wider text-base-content/50 mb-3 px-1">
                <User size={16} /> Dados do Aluno
              </h4>
              <div className="grid gap-4 border border-base-300 sm:grid-cols-2 bg-base-200/30 p-4 rounded-xl">
                <SummaryItem className="col-span-2" label="E-mail" value={alunoQuery.data.email} />
                <SummaryItem label="CPF" value={formatCpf(alunoQuery.data.cpf)} />
                <SummaryItem label="Nascimento" value={formatDateShortYear(alunoQuery.data.dataNascimento)} />
                <SummaryItem label="Contato" value={formatPhone(alunoQuery.data.telefone)} />
                <SummaryItem label="Data Matrícula" value={formatDateShortYear(alunoQuery.data.createdAt)} />
              </div>
            </div>

            <div className="animate-[fade-up_600ms_ease-out_both]">
              <h4 className="flex items-center gap-2 text-sm font-bold uppercase tracking-wider text-base-content/50 mb-3 px-1">
                <Handshake size={16} /> Responsável
              </h4>
              <div className="grid gap-4 border border-base-300 sm:grid-cols-2 bg-base-200/30 p-4 rounded-xl">
                <SummaryItem className="col-span-2" label="Nome Completo" value={responsavelQuery.data.nome} />
                <SummaryItem label="CPF" value={formatCpf(responsavelQuery.data.cpf)} />
                <SummaryItem label="Contato" value={formatPhone(responsavelQuery.data.telefone)} />
                <SummaryItem className="col-span-2" label="E-mail" value={responsavelQuery.data.email} />
              </div>
            </div>
          </div>

          <Collapse
            title="Endereço"
            className="mt-2 shadow-sm border border-base-300 hover:bg-base-200/50 transition-colors animate-[fade-up_700ms_ease-out_both]"
          >
            <AddressDetails address={alunoQuery.data.endereco} />
          </Collapse>
        </div>
      </div>
    </section>
  );
};
