import { Button } from "@/components/ui/button";
import { Collapse } from "@/components/ui/collapse";
import { ErrorCard } from "@/components/ui/error-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { AddressDetails } from "@/lib/shared/address/components/AddressDetails";
import { useFindColaboradorById } from "@/kubb";
import { formatCpf, formatDateShortYear, formatPhone } from "@/lib/utils/formatter";
import { Edit, User } from "lucide-react";
import { ArquivarColaboradorButton } from "./ArquivarColaboradorButton";
import { DeletarColaboradorButton } from "./DeletarColaboradorButton";
import { dutyLabels } from "../lib/dutyLabels";

interface ColaboradorInfoSectionProps {
  colaboradorId: string;
  onEdit: () => void;
}

export function ColaboradorInfoSection({ colaboradorId, onEdit }: ColaboradorInfoSectionProps) {
  const colaboradorQuery = useFindColaboradorById(colaboradorId);

  if (colaboradorQuery.error) {
    return <ErrorCard title="Erro ao carregar detalhes do colaborador" error={colaboradorQuery.error} />;
  }

  if (colaboradorQuery.isPending || !colaboradorQuery.data) {
    return (
      <SectionCard title="Colaborador" description="Dados do Colaborador">
        <div className="h-48 w-full animate-pulse rounded-lg bg-base-300/50" />
      </SectionCard>
    );
  }

  const isArchived = colaboradorQuery.data.active === false;

  return (
    <section className={`p-4 rounded-xl border border-base-300 bg-base-100 shadow-sm`}>
      <div className="flex items-center justify-between mb-3">
          <div className="flex items-center gap-3">
             <span className="font-bold text-2xl text-base-content">{`${colaboradorQuery.data.name}`}</span>
              <span className={`text-xs md:text-md badge ${isArchived ? "badge-ghost" : "badge-success"} badge-md gap-1`}>
                {isArchived ? "Arquivado" : "Ativo"}
              </span>
          </div>

          <div className="flex gap-2 items-center">
            <Button onClick={onEdit} variant="primary">
              <Edit className="h-4 w-4 mr-2" />Editar
            </Button>

            <ArquivarColaboradorButton
              colaboradorId={colaboradorQuery.data.id}
              isArchived={isArchived}
            />
            <DeletarColaboradorButton colaboradorId={colaboradorQuery.data.id} />
          </div>
        </div>

        <h4 className="flex items-center gap-2 text-sm font-bold uppercase text-base-content/50 mb-3 px-1">
          <User size={16} />
          Dados do Colaborador
        </h4>
        <div className="grid gap-4 rounded-xl border border-base-300 bg-base-200/30 p-4 md:grid-cols-2 xl:grid-cols-3">
          <SummaryItem label="Nome completo" value={colaboradorQuery.data.name} />
          <SummaryItem label="E-mail" value={colaboradorQuery.data.email} />
          <SummaryItem label="Cargo" value={dutyLabels[colaboradorQuery.data.duty] ?? "Desconhecido"} />
          <SummaryItem label="Contato" value={formatPhone(colaboradorQuery.data.contact)} />
          <SummaryItem label="CPF" value={formatCpf(colaboradorQuery.data.cpf)} />
          <SummaryItem label="Data de nascimento" value={formatDateShortYear(colaboradorQuery.data.birthdate ?? "")} />
          <SummaryItem label="Chave PIX" value={colaboradorQuery.data.pix} />
          <SummaryItem className="grow" label="Criado em" value={formatDateShortYear(colaboradorQuery.data.createdAt ?? "")} />
        </div>

        <Collapse
          title="Endereço"
          className="mt-4 shadow-sm border border-base-300 hover:bg-base-200/50 transition-colors animate-[fade-up_700ms_ease-out_both]"
        >
          <AddressDetails address={colaboradorQuery.data.address} />
        </Collapse>
      </section>
  );
}
