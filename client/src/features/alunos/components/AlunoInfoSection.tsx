import { Button } from "@/components/ui/button";
import { Collapse } from "@/components/ui/collapse";
import { SummaryItem } from "@/components/ui/summary-item";
import { AddressDetails } from "@/lib/shared/address/components/AddressDetails";
import {
  useBuscarResponsavelPorId,
  useBuscarAlunoPorId,
} from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { Edit, Handshake, User } from "lucide-react";
import { ArquivarAlunoButton } from "./ArquivarAlunoButton";
import { DeletarAlunoButton } from "./DeletarAlunoButton";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";

interface AlunoInfoSectionProps {
  studentId: string;
  onEdit: () => void;
}

export const AlunoInfoSection = ({studentId,onEdit}: AlunoInfoSectionProps) => {
  const alunoQuery = useBuscarAlunoPorId(studentId);
  const responsavelQuery = useBuscarResponsavelPorId(alunoQuery.data?.parentId || "");

  if (alunoQuery.error || responsavelQuery.error) {
    return <ErrorCard title="Erro ao carregar detalhes do aluno" error={alunoQuery.error || responsavelQuery.error} />;
  }

  if (alunoQuery.isPending || !alunoQuery.data || responsavelQuery.isPending || !responsavelQuery.data) {
    return <LoadingCard title="Carregando detalhes do aluno" />;
  }

  return (
    <section className={`card rounded-xl border border-base-300 bg-base-100 shadow-sm`}>
      <div className={`card-body`}>
        <div className="card-actions justify-between">
          <div className="card-title text-2xl font-bold text-base-content">
            {`${alunoQuery.data.name}`}
              <span className={`text-xs md:text-md badge ${alunoQuery.data.active ? "badge-success" : "badge-ghost"} badge-md gap-1`}>
                {alunoQuery.data.active ? "Ativo" : "Arquivado"}
              </span>
              <span className="text-xs md:text-md badge badge-outline badge-md">{alunoQuery.data.age} anos</span>
              <span className="text-xs md:text-md badge badge-outline badge-md">{alunoQuery.data.school}</span>
          </div>

          <div className="flex gap-2 items-center justify-start">
            <Button onClick={onEdit} variant="primary" size="sm" className="sm:btn-md">
              <Edit className="h-4 w-4 mr-1 sm:mr-2" />
              Editar
            </Button>
            <ArquivarAlunoButton className="btn-sm md:btn-md" studentId={alunoQuery.data.id} active={alunoQuery.data.active!} />
            <DeletarAlunoButton className="btn-sm md:btn-md" studentId={alunoQuery.data.id} />
          </div>
        </div>

        <div className="flex flex-col gap-8">
          <div className="grid gap-8 md:grid-cols-2">
            {/* Dados do Aluno */}
            <div className="animate-[fade-up_500ms_ease-out_both]">
              <h4 className="flex items-center gap-2 text-sm font-bold uppercase tracking-wider text-base-content/50 mb-3 px-1">
                <User size={16} /> Dados do Aluno
              </h4>
              <div className="grid gap-4 border border-base-300 sm:grid-cols-2 bg-base-200/30 p-4 rounded-xl">
                <SummaryItem className="col-span-2" label="E-mail" value={alunoQuery.data.email} />
                <SummaryItem label="CPF" value={formatCpf(alunoQuery.data.cpf)}/>
                <SummaryItem label="Nascimento" value={formatDateShortYear(alunoQuery.data.birthdate)} />
                <SummaryItem label="Contato" value={formatPhone(alunoQuery.data.contact)} />
                <SummaryItem label="Data Matrícula" value={formatDateShortYear(alunoQuery.data.createdAt)} />
              </div>
            </div>

            {/* Dados do Responsável */}
            <div className="animate-[fade-up_600ms_ease-out_both]">
              <h4 className="flex items-center gap-2 text-sm font-bold uppercase tracking-wider text-base-content/50 mb-3 px-1">
                <Handshake size={16} /> Responsável
              </h4>
              <div className="grid gap-4 border border-base-300 sm:grid-cols-2 bg-base-200/30 p-4 rounded-xl">
                <SummaryItem className="col-span-2" label="Nome Completo" value={responsavelQuery.data?.name} />
                <SummaryItem label="CPF" value={formatCpf(responsavelQuery.data?.cpf)} />
                <SummaryItem label="Contato" value={formatPhone(responsavelQuery.data?.contact)} />
                <SummaryItem className="col-span-2" label="E-mail" value={responsavelQuery.data?.email} />
              </div>
            </div>
          </div>

          <Collapse
            title={"Endereço"}
            className="mt-2 shadow-sm border border-base-300 hover:bg-base-200/50 transition-colors animate-[fade-up_700ms_ease-out_both]"
          >
            <AddressDetails address={alunoQuery.data?.address} />
          </Collapse>
        </div>
      </div>
    </section>
  );
};
