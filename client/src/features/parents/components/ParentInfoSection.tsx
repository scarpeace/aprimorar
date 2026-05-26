import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { useBuscarResponsavelPorId } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { Edit, Handshake, UserRound } from "lucide-react";
import { ArchiveParentButton } from "./ArchiveParentButton";
import { DeleteParentButton } from "./DeleteParentButton";

interface ParentInfoSectionProps {
  parentId: string;
  onEdit: () => void;
}

export function ParentInfoSection({ parentId, onEdit }: Readonly<ParentInfoSectionProps>) {
  const parentQuery = useBuscarResponsavelPorId(parentId);

  if (parentQuery.error) {
    return (
      <ErrorCard
        title="Erro ao carregar detalhes do responsável"
        error={parentQuery.error}
      />
    );
  }

  if (parentQuery.isPending || !parentQuery.data) {
    return <LoadingCard title="Carregando detalhes do responsável" />;
  }

  const parent = parentQuery.data;
  const isArchived = parent.active === false;

  return (
    <section className="card rounded-xl border border-base-300 bg-base-100 shadow-sm">
      <div className="card-body gap-6">
        <div className="flex flex-col justify-between gap-4 md:flex-row md:items-start">
          <div>
            <div className="flex flex-wrap items-center gap-2">
              <h2 className="card-title text-xl font-bold text-base-content">
                {parent.name}
              </h2>
              <span className={`badge ${isArchived ? "badge-ghost" : "badge-success"} badge-md`}>
                {isArchived ? "Inativo" : "Ativo"}
              </span>
            </div>
            <p className="mt-1 text-sm text-base-content/60">
              Dados cadastrais e vínculos do responsável.
            </p>
          </div>

          <div className="flex flex-wrap gap-2">
            <Button onClick={onEdit} variant="primary">
              <Edit className="h-4 w-4" />
              Editar
            </Button>
            <ArchiveParentButton parentId={parent.parentId} isArchived={isArchived} />
            <DeleteParentButton parentId={parent.parentId} />
          </div>
        </div>

        <div className="grid gap-8 md:grid-cols-2">
          <div className="animate-[fade-up_500ms_ease-out_both]">
            <h4 className="mb-3 flex items-center gap-2 px-1 text-sm font-bold uppercase tracking-wider text-base-content/50">
              <UserRound size={16} /> Dados do Responsável
            </h4>
            <div className="grid gap-4 rounded-xl border border-base-300 bg-base-200/30 p-4 sm:grid-cols-2">
              <SummaryItem className="sm:col-span-2" label="Nome completo" value={parent.name} />
              <SummaryItem label="CPF" value={formatCpf(parent.cpf)} />
              <SummaryItem label="Contato" value={formatPhone(parent.contact)} />
              <SummaryItem className="sm:col-span-2" label="E-mail" value={parent.email} />
            </div>
          </div>

          <div className="animate-[fade-up_600ms_ease-out_both]">
            <h4 className="mb-3 flex items-center gap-2 px-1 text-sm font-bold uppercase tracking-wider text-base-content/50">
              <Handshake size={16} /> Cadastro
            </h4>
            <div className="grid gap-4 rounded-xl border border-base-300 bg-base-200/30 p-4 sm:grid-cols-2">
              <SummaryItem
                label="Nascimento"
                value={parent.birthdate ? formatDateShortYear(parent.birthdate) : "Não informado"}
              />
              <SummaryItem
                label="Criado em"
                value={formatDateShortYear(parent.createdAt)}
              />
              <SummaryItem
                label="Pix"
                value={parent.pix || "Não informado"}
              />
              <SummaryItem
                label="Status"
                value={isArchived ? "Inativo" : "Ativo"}
              />
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
