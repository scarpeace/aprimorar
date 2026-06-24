import { Button } from "@/components/Ui/Button.tsx";
import { Collapse } from "@/components/Ui/Collapse.tsx";
import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { SummaryItem } from "@/components/Ui/SummaryItem.tsx";
import { AddressDetails } from "@/components/Ui/AddressDetails.tsx";
import { useFindColaboradorById } from "@/kubb";
import { formatCpf, formatPhone } from "@/utils/formatter.ts";
import { formatDateShortYear } from "@/utils/date-utils.ts";
import { Edit, User } from "lucide-react";
import { ArchiveColaboradorButton } from "./ArchiveColaboradorButton.tsx";
import { DeleteColaboradorButton } from "./DeleteColaboradorButton.tsx";
import { colaboradorConstants } from "../../utils/constants/colaborador-constants.ts";

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
      <h1>Olá</h1>
    );
  }

  const isArchived = colaboradorQuery.data.active === false;

  return (
     <section className="p-4 rounded-xl border border-base-300 bg-base-100 shadow-sm">
      <div className="flex items-center justify-between mb-3">
          <div className="flex items-center gap-3">
             <span className="font-bold text-2xl text-base-content">{colaboradorQuery.data.nome}</span>
              <span className={`text-xs md:text-md badge ${isArchived ? "badge-ghost" : "badge-success"} badge-md gap-1`}>
                {isArchived ? "Arquivado" : "Ativo"}
              </span>
          </div>

          <div className="flex gap-2 items-center">
            <Button onClick={onEdit} variant="primary">
              <Edit className="h-4 w-4 mr-2" />Editar
            </Button>

            <ArchiveColaboradorButton
              colaboradorId={colaboradorQuery.data.id}
              isArchived={isArchived}
            />
            <DeleteColaboradorButton colaboradorId={colaboradorQuery.data.id} />
          </div>
        </div>

        <h4 className="flex items-center gap-2 text-sm font-bold uppercase text-base-content/50 mb-3 px-1">
          <User size={16} />
          Dados do Colaborador
        </h4>
        <div className="grid gap-4 rounded-xl border border-base-300 bg-base-200/30 p-4 md:grid-cols-2 xl:grid-cols-3">
          <SummaryItem label="Nome completo" value={colaboradorQuery.data.nome} />
          <SummaryItem label="E-mail" value={colaboradorQuery.data.email} />
          <SummaryItem label="Cargo" value={colaboradorConstants[colaboradorQuery.data.funcao] ?? "Desconhecido"} />
          <SummaryItem label="Contato" value={formatPhone(colaboradorQuery.data.telefone)} />
          <SummaryItem label="CPF" value={formatCpf(colaboradorQuery.data.cpf)} />
          <SummaryItem label="Data de nascimento" value={formatDateShortYear(colaboradorQuery.data.dataNascimento ?? "")} />
          <SummaryItem label="Chave PIX" value={colaboradorQuery.data.pix} />
          <SummaryItem className="grow" label="Criado em" value={formatDateShortYear(colaboradorQuery.data.createdAt ?? "")} />
        </div>

        <Collapse
          title="Endereço"
          className="mt-4 shadow-sm border border-base-300 hover:bg-base-200/50 transition-colors animate-[fade-up_700ms_ease-out_both]"
        >
          <AddressDetails address={colaboradorQuery.data.endereco} />
        </Collapse>
      </section>
  );
}
