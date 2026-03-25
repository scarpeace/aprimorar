import { Table, type ColumnDef } from "@/components/ui/table";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import type { EventResponseDTO, PagedModelEventResponseDTO } from "@/kubb";
import type { ReactNode } from "react";

type EventTableContext = "default" | "student" | "employee";

type EventTableProps = {
  variant?: "page" | "embedded";
  context?: EventTableContext;
  data?: PagedModelEventResponseDTO;
  isLoading: boolean;
  error: unknown;
  currentPage: number;
  onPageChange: (page: number) => void;
  itemName?: string;
  renderActions?: (event: EventResponseDTO) => ReactNode;
};

function getEventColumns(context: EventTableContext): ColumnDef<EventResponseDTO>[] {
  const baseColumns: ColumnDef<EventResponseDTO>[] = [
    {
      header: "Título",
      accessor: (event) => event.title,
    },
    {
      header: "Conteúdo",
      accessor: (event) =>event.content,
    },
    {
      header: "Data",
      accessor: (event) => formatDateShortYear(event.startDate),
    },
    {
      header: "Hora",
      accessor: (event) => formatTime(event.startDate),
    },
  ];

  if (context !== "student") {
    baseColumns.push({
      header: "Aluno",
      accessor: (event) => event.studentName,
    });
  }

  if (context !== "employee") {
    baseColumns.push({
      header: "Colaborador",
      accessor: (event) => event.employeeName,
    });
  }

  baseColumns.push(
    context === "employee"
      ? {
          header: "Pagamento",
          accessor: (event) => brl.format(event.payment),
          headerClassName: "text-right",
          cellClassName: "text-right",
        }
      : {
          header: "Preço",
          accessor: (event) => brl.format(event.price),
          headerClassName: "text-right",
          cellClassName: "text-right",
        },
  );

  return baseColumns;
}

export function EventTable({
  variant = "page",
  context = "default",
  data,
  isLoading,
  error,
  currentPage,
  onPageChange,
  itemName = "eventos",
  renderActions,
}: Readonly<EventTableProps>) {
  return (
    <Table
      variant={variant}
      data={data}
      columns={getEventColumns(context)}
      isLoading={isLoading}
      error={error}
      currentPage={currentPage}
      onPageChange={onPageChange}
      itemName={itemName}
      getRowKey={(event) => event.eventId}
      renderActions={renderActions}
      loadingDescription="Carregando eventos..."
      emptyTitle="Nenhum evento encontrado."
      emptyDescription="Nenhum evento encontrado para os filtros aplicados."
      errorTitle="Erro ao carregar eventos"
    />
  );
}
