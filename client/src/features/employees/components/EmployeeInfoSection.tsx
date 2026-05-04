import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { useGetEmployeeById } from "@/kubb";
import { formatCpf, formatDateShortYear, formatPhone } from "@/lib/utils/formatter";
import { Edit } from "lucide-react";
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
    return (
      <ErrorCard
        title="Erro ao carregar detalhes do colaborador"
        error={employeeQuery.error}
      />
    );
  }

  if (employeeQuery.isPending || !employeeQuery.data) {
    return (
      <SectionCard title="Colaborador" description="Dados do Colaborador">
        <div className="h-48 w-full animate-pulse rounded-lg bg-base-300/50" />
      </SectionCard>
    );
  }

  const employee = employeeQuery.data;

  return (
    <SectionCard
      title="Colaborador"
      description="Dados do Colaborador"
      headerActions={
        <div className="flex gap-2 items-center flex-wrap justify-end">
          <Button onClick={onEdit} variant="primary">
            <Edit className="h-4 w-4 mr-2" />Editar
          </Button>

          <ArchiveEmployeeButton
            employeeId={employee.id}
            isArchived={!!employee.archivedAt}
          />
          <DeleteEmployeeButton employeeId={employee.id} />
        </div>
      }
    >
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        <SummaryItem label="Nome completo" value={employee.name} />
        <SummaryItem label="E-mail" value={employee.email} />
        <SummaryItem label="Cargo" value={dutyLabels[employee.duty] ?? "Desconhecido"} />
        <SummaryItem label="Contato" value={formatPhone(employee.contact)} />
        <SummaryItem label="CPF" value={formatCpf(employee.cpf)} />
        <SummaryItem label="Data de nascimento" value={formatDateShortYear(employee.birthdate ?? "")} />
        <SummaryItem label="Chave PIX" value={employee.pix} />
        <SummaryItem label="Status" value={employee.archivedAt ? "Arquivado" : "Ativo"} />
        <SummaryItem className="grow" label="Criado em" value={formatDateShortYear(employee.createdAt ?? "")} />
      </div>
    </SectionCard>
  );
}
