import { ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { EventsTable } from "@/features/events/components/EventsTable";
import {
  employeeResponseDTODutyEnum,
  useGetEmployeeById,
  useGetEventsByEmployee,
} from "@/kubb";
import { formatDateShortYear } from "@/lib/utils/formatter";
import { Edit, FileUser } from "lucide-react";
import { useState, type ReactNode } from "react";
import { useParams } from "react-router-dom";
import { ArchiveEmployeeButton } from "../components/ArchiveEmployeeButton";
import { DeleteEmployeeButton } from "../components/DeleteEmployeeButton";

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>();
  const employeeId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: employee,
    error: employeeError,
    isPending: isEmployeePending,
  } = useGetEmployeeById(employeeId);

  const {
    data: employeeEvents,
    isLoading: isEmployeeEventsLoading,
    error: employeeEventsError,
  } = useGetEventsByEmployee(employeeId);

  const isEmployeeLoading = isEmployeePending || !employee;
  const hasEmployeeError = !!employeeError;

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: employee?.name },
    { label: "E-mail", value: employee?.email },
    {
      label: "Cargo",
      value:
        employeeResponseDTODutyEnum[
          employee?.duty as keyof typeof employeeResponseDTODutyEnum
        ] ?? "Desconhecido",
    },
    { label: "Contato", value: employee?.contact },
    { label: "CPF", value: employee?.cpf },
    { label: "Chave PIX", value: employee?.pix },
    {
      label: "Data de nascimento",
      value: formatDateShortYear(employee?.birthdate ?? ""),
    },
    {
      label: "Status",
      value: employee?.archivedAt ? "Arquivado" : "Ativo",
    },
    {
      label: "Criado em",
      value: formatDateShortYear(employee?.createdAt ?? ""),
    },
  ];

  return (
    <>
      <PageHeader
        description="Veja e gerencie as informações do colaborador"
        title="Detalhes do colaborador"
        Icon={FileUser}
        backLink="/students"
      />

      {hasEmployeeError ? (
        <ErrorCard
          title="Erro ao carregar detalhes do colaborador"
          error={employeeError}
        />
      ) : isEmployeeLoading ? (
        <LoadingCard title="Carregando detalhes do colaborador" />
      ) : (
        <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
          <SectionCard
            title="Colaborador "
            description="Dados do Colaborador"
            headerActions={
              <>
                <ButtonLink
                  to={`/employees/edit/${employee.id}`}
                  variant="primary"
                >
                  <Edit className="h-4 w-4" />
                  Editar
                </ButtonLink>

                <ArchiveEmployeeButton
                  employeeId={employee.id}
                  isArchived={!!employee.archivedAt}
                />
                <DeleteEmployeeButton employeeId={employee.id} />
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
              eventsPage={employeeEvents}
              isPending={isEmployeeEventsLoading}
              error={employeeEventsError}
              currentPage={currentPage}
              onPageChange={setCurrentPage}
            />
          </SectionCard>
        </div>
      )}
    </>
  );
}
