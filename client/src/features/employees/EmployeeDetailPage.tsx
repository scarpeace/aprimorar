import type { ReactNode } from "react";
import { UserCog } from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import { ErrorCard } from "@/components/ui/error-card";
import { PageHeader } from "@/components/ui/page-header";
import { PageLoading } from "@/components/ui/page-loading";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { dutyLabels } from "@/features/employees/schemas/dutyEnum";
import { EventsTable } from "@/features/events/components/EventsTable";
import styles from "@/features/employees/EmployeeDetailPage.module.css";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { useEmployeeDetailQuery } from "@/features/employees/query/useEmployeeQueries";
import { DeleteEmployeeButton } from "./components/DeleteEmployeeButton";
import { EditEmployeeButton } from "./components/EditEmployeeButton";
import { ArchiveEmployeeButton } from "./components/ArchiveEmployeeButton";
import { ButtonLink } from "@/components/ui/button";
import { formatDateShortYear } from "@/lib/utils/formatter";

//TODO: Tá renderizando duas (ou quatro não sei) vezes
export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>();
  const employeeId = id ?? "";
  const navigate = useNavigate();

  const {
    data: employeeData,
    error: employeeDataError,
    isLoading: isEmployeeLoading,
    isFetched: isEmployeeFetched,
  } = useEmployeeDetailQuery(employeeId);

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: employeeData?.name },
    { label: "E-mail", value: employeeData?.email },
    {
      label: "Cargo",
      value: dutyLabels[employeeData?.duty as keyof typeof dutyLabels],
    },
    { label: "Contato", value: employeeData?.contact },
    { label: "CPF", value: employeeData?.cpf },
    { label: "Chave PIX", value: employeeData?.pix },
    {
      label: "Data de nascimento",
      value: formatDateShortYear(employeeData?.birthdate ?? ""),
    },
    {
      label: "Status",
      value: employeeData?.archivedAt ? "Arquivado" : "Ativo",
    },
    {
      label: "Criado em",
      value: formatDateShortYear(employeeData?.createdAt ?? ""),
    },
  ];

  //Ao implementar a tabela geral isso aqui vai funcionar.
  // const myEventsColumns: ColumnDef<EventResponse>[] = [
  //   { header: "Aluno", accessor: (event) => event.studentName },
  //   {
  //     header: "Data",
  //     accessor: (event) => formatDateShortYear(event.startDate),
  //   },
  //   {
  //     header: "Horário",
  //     accessor: (event) =>
  //       `${formatTime(event.startDate)} às ${formatTime(event.endDate)}`,
  //   },
  //   {
  //     header: "Conteúdo",
  //     accessor: (event) => eventContentLabels[event.content],
  //   },
  //   { header: "Pagamento", accessor: (event) => brl.format(event.payment) },
  // ];

  return (
    <div className={styles.page}>
      <PageHeader
        description="Gerencie professores e equipe."
        title="Colaboradores"
        Icon={UserCog}
        action={
          <ButtonLink
            className="sm:ml-auto"
            to="/employees/new"
            variant="success"
          >
            Novo colaborador
          </ButtonLink>
        }
      />

      {/* RESUMO DO COLABORADOR */}
      <SectionCard
        title="Resumo do colaborador"
        description="Dados completos de cadastro, contato e status."
        headerAction={
          <>
            <EditEmployeeButton employeeId={employeeId} />
            <ArchiveEmployeeButton
              employeeId={employeeId}
              isArchived={!!employeeData?.archivedAt}
            />
            <DeleteEmployeeButton employeeId={employeeId} />
          </>
        }
      >
        {isEmployeeLoading && (
          <PageLoading message="Carregando colaborador..." />
        )}

        {employeeDataError && (
          <div className={styles.page}>
            <ErrorCard
              description={getFriendlyErrorMessage(employeeDataError)}
              actionLabel="Voltar para listagem de colaboradores"
              onAction={() => navigate("/employees")}
            />
          </div>
        )}

        {isEmployeeFetched && (
          <div className={styles.summaryGrid}>
            {summaryItems.map((item) => (
              <SummaryItem
                key={item.label}
                label={item.label}
                value={item.value}
              />
            ))}
          </div>
        )}
      </SectionCard>

      {/* EVENTOS DO COLABORADOR */}
      <SectionCard
        title="Eventos vinculados"
        description={`Total de eventos vinculados ao colaborador: ${employeeData?.name}`}
      >
        <EventsTable variant={"embeddedEmployee"} ownerId={employeeId} />
      </SectionCard>
    </div>
  );
}
