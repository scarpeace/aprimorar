import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { useGetEmployeeById } from "@/kubb";
import { formatCpf, formatDateShortYear, formatPhone } from "@/lib/utils/formatter";
import { Edit, User } from "lucide-react";
import { ArchiveEmployeeButton } from "./ArchiveEmployeeButton";
import { DeleteEmployeeButton } from "./DeleteEmployeeButton";
import { dutyLabels } from "../utils/dutyLabels";

interface EmployeeInfoSectionProps {
  employeeId: string;
  onEdit: () => void;
}

export function EmployeeInfoSection({ employeeId, onEdit }: EmployeeInfoSectionProps) {
  const employeeQuery = useGetEmployeeById(employeeId);

  if (employeeQuery.error) {
    return <ErrorCard title="Erro ao carregar detalhes do colaborador" error={employeeQuery.error} />;
  }

  if (employeeQuery.isPending || !employeeQuery.data) {
    return (
      <SectionCard title="Colaborador" description="Dados do Colaborador">
        <div className="h-48 w-full animate-pulse rounded-lg bg-base-300/50" />
      </SectionCard>
    );
  }

  return (
    <section className={`p-4 rounded-xl border border-base-300 bg-base-100 shadow-sm`}>
      <div className="flex items-center justify-between mb-3">
          <div className="flex items-center gap-3">
             <span className="font-bold text-2xl text-base-content">{`${employeeQuery.data.name}`}</span>
              <span className={`text-xs md:text-md badge ${employeeQuery.data.archivedAt ? "badge-ghost" : "badge-success"} badge-md gap-1`}>
                {employeeQuery.data.archivedAt ? "Arquivado" : "Ativo"}
              </span>
          </div>

          <div className="flex gap-2 items-center">
            <Button onClick={onEdit} variant="primary">
              <Edit className="h-4 w-4 mr-2" />Editar
            </Button>

            <ArchiveEmployeeButton
              employeeId={employeeQuery.data.id}
              isArchived={!!employeeQuery.data.archivedAt}
            />
            <DeleteEmployeeButton employeeId={employeeQuery.data.id} />
          </div>
        </div>

        <h4 className="flex items-center gap-2 text-sm font-bold uppercase text-base-content/50 mb-3 px-1">
          <User size={16} />
          Dados do Colaborador
        </h4>
        <div className="grid gap-4 rounded-xl border border-base-300 bg-base-200/30 p-4 md:grid-cols-2 xl:grid-cols-3">
          <SummaryItem label="Nome completo" value={employeeQuery.data.name} />
          <SummaryItem label="E-mail" value={employeeQuery.data.email} />
          <SummaryItem label="Cargo" value={dutyLabels[employeeQuery.data.duty] ?? "Desconhecido"} />
          <SummaryItem label="Contato" value={formatPhone(employeeQuery.data.contact)} />
          <SummaryItem label="CPF" value={formatCpf(employeeQuery.data.cpf)} />
          <SummaryItem label="Data de nascimento" value={formatDateShortYear(employeeQuery.data.birthdate ?? "")} />
          <SummaryItem label="Chave PIX" value={employeeQuery.data.pix} />
          <SummaryItem className="grow" label="Criado em" value={formatDateShortYear(employeeQuery.data.createdAt ?? "")} />
        </div>
      </section>
  );
}
