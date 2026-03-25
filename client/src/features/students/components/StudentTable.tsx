import { Table, type ColumnDef } from "@/components/ui/table";
import { formatDateShortYear, formatPhone } from "@/lib/utils/formatter";
import type { StudentResponseDTO, PagedModelStudentResponseDTO } from "@/kubb";
import type { ReactNode } from "react";

type StudentTableProps = {
  variant?: "page" | "embedded";
  data?: PagedModelStudentResponseDTO;
  isLoading: boolean;
  error: unknown;
  currentPage: number;
  onPageChange: (page: number) => void;
  itemName?: string;
  renderActions?: (student: StudentResponseDTO) => ReactNode;
};

function getStudentColumns(
  variant: "page" | "embedded",
): ColumnDef<StudentResponseDTO>[] {
  const baseColumns: ColumnDef<StudentResponseDTO>[] = [
    {
      header: "Nome",
      accessor: (student) => student.name,
    },
    {
      header: "CPF",
      accessor: (student) => student.cpf,
    },
    {
      header: "Nascimento",
      accessor: (student) => formatDateShortYear(student.birthdate),
    },
    {
      header: "Contato",
      accessor: (student) => formatPhone(student.contact),
    },
    {
      header: "Escola",
      accessor: (student) => student.school,
    },
    {
      header: "Matrícula",
      accessor: (student) => formatDateShortYear(student.createdAt),
    },
    {
      header: "Status",
      accessor: (student) => (student.archivedAt ? "Arquivado" : "Ativo"),
    },
  ];

  // if (variant !== "embedded") {
  //   baseColumns.push({
  //     header: "Aluno",
  //     accessor: (student) => student.name,
  //   });
  // }

  // if (variant !== "page") {
  //   baseColumns.push({
  //     header: "ESSE CAMPO É !==",
  //     accessor: (student) => student.school,
  //   });
  // }

  // baseColumns.push(
  //   context === "employee"
  //     ? {
  //         header: "Pagamento",
  //         accessor: (student) => brl.format(student.payment),
  //         headerClassName: "text-right",
  //         cellClassName: "text-right",
  //       }
  //     : {
  //         header: "Preço",
  //         accessor: (student) => brl.format(student.price),
  //         headerClassName: "text-right",
  //         cellClassName: "text-right",
  //       },
  // );

  return baseColumns;
}

export function StudentTable({
  variant = "page",
  data,
  isLoading,
  error,
  currentPage,
  onPageChange,
  itemName = "studentos",
  renderActions,
}: Readonly<StudentTableProps>) {
  return (
    <Table
      variant={variant}
      data={data}
      columns={getStudentColumns(variant)}
      isLoading={isLoading}
      error={error}
      currentPage={currentPage}
      onPageChange={onPageChange}
      itemName={itemName}
      getRowKey={(student) => student.id}
      renderActions={renderActions}
      loadingDescription="Carregando studentos..."
      emptyTitle="Nenhum studento encontrado."
      emptyDescription="Nenhum studento encontrado para os filtros aplicados."
      errorTitle="Erro ao carregar studentos"
    />
  );
}
