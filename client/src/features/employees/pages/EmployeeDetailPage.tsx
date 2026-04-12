import { ButtonLink } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { EventsTable } from "@/features/events/components/EventsTable";
import { useGetEmployeeById, useGetEventsByEmployee } from "@/kubb";
import { formatDateShortYear } from "@/lib/utils/formatter";
import { Edit, FileUser } from "lucide-react";
import { useState, type ReactNode } from "react";
import { useParams } from "react-router-dom";
import { ArchiveEmployeeButton } from "../components/ArchiveEmployeeButton";
import { DeleteEmployeeButton } from "../components/DeleteEmployeeButton";

const employeeDutyLabels = {
  TEACHER: "Professor",
  ADM: "Administrativo",
  THERAPIST: "Terapeuta",
  MENTOR: "Mentor",
  SYSTEM: "Sistema",
} as const;

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>();
  const employeeId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const employeeQuery = useGetEmployeeById(employeeId);
  const employeeEventsQuery = useGetEventsByEmployee(employeeId);

  const headerProps = {
    description: "Veja e gerencie as informações do colaborador",
    title: "Detalhes do colaborador",
    Icon: FileUser,
    backLink: "/employees",
  };

  if (employeeQuery.error) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar detalhes do colaborador"
          error={employeeQuery.error}
        />
      </PageLayout>
    );
  }

  if (employeeQuery.isPending || !employeeQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando detalhes do colaborador" />
      </PageLayout>
    );
  }

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: employeeQuery.data.name },
    { label: "E-mail", value: employeeQuery.data.email },
    {
      label: "Cargo",
      value: employeeDutyLabels[employeeQuery.data.duty] ?? "Desconhecido",
    },
    { label: "Contato", value: employeeQuery.data.contact },
    { label: "CPF", value: employeeQuery.data.cpf },
    { label: "Chave PIX", value: employeeQuery.data.pix },
    {
      label: "Data de nascimento",
      value: formatDateShortYear(employeeQuery.data.birthdate ?? ""),
    },
    {
      label: "Status",
      value: employeeQuery.data.archivedAt ? "Arquivado" : "Ativo",
    },
    {
      label: "Criado em",
      value: formatDateShortYear(employeeQuery.data.createdAt ?? ""),
    },
  ];

  return (
    <PageLayout {...headerProps}>
      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <SectionCard
          title="Colaborador "
          description="Dados do Colaborador"
          headerActions={
            <>
              <ButtonLink
                to={`/employees/edit/${employeeQuery.data.id}`}
                variant="primary"
              >
                <Edit className="h-4 w-4" />
                Editar
              </ButtonLink>

              <ArchiveEmployeeButton
                employeeId={employeeQuery.data.id}
                isArchived={!!employeeQuery.data.archivedAt}
              />
              <DeleteEmployeeButton employeeId={employeeQuery.data.id} />
            </>
          }
        >
          <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
            {summaryItems.map((item) => (
              <SummaryItem
                key={item.label}
                label={item.label}
                value={item.value}
              />
            ))}
          </div>

          {/*<Collapse title={"Endereço"} className="mt-3">
            <AddressDetails address={employeeData.address} />
          </Collapse>*/}
        </SectionCard>

        <SectionCard
          title={"Atendimentos"}
          description={"Atendimentos vinculados ao colaborador"}
        >
          <EventsTable
            eventsPage={employeeEventsQuery.data}
            isPending={employeeEventsQuery.isPending}
            error={employeeEventsQuery.error}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
          />
        </SectionCard>
      </div>
    </PageLayout>
  );
}
