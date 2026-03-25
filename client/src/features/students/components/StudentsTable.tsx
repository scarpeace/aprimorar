import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { Pagination } from "@/components/ui/pagination";
import type { StudentResponseDTO } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  formatCpf,
  formatPhone,
  formatDateShortYear,
} from "@/lib/utils/formatter";
import type { ReactNode } from "react";

type StudentsTableRootProps = {
  children: ReactNode;
};

type StudentsTableStateProps = {
  students?: StudentResponseDTO[];
  isLoading: boolean;
  error: unknown;
  children: ReactNode;
};

type StudentsTableContentProps = {
  students: StudentResponseDTO[];
  renderActions?: (student: StudentResponseDTO) => ReactNode;
};

type StudentsTablePaginationProps = {
  currentPage: number;
  totalElements: number;
  totalPages: number;
  currentElementsCount: number;
  onPageChange: (page: number) => void;
  itemName?: string;
};

export function StudentsTableRoot({
  children,
}: Readonly<StudentsTableRootProps>) {
  return <div className="app-table-wrap overflow-x-auto">{children}</div>;
}

export function StudentsTableState({
  students,
  isLoading,
  error,
  children,
}: Readonly<StudentsTableStateProps>) {
  if (isLoading) {
    return <LoadingCard description="Carregando alunos..." />;
  }
  if (error) {
    return (
      <ErrorCard
        title="Erro ao carregar alunos"
        description={getFriendlyErrorMessage(error)}
      />
    );
  }

  if (!students || students.length === 0) {
    return (
      <EmptyCard
        title="Nenhum aluno encontrado."
        description="Nenhum aluno encontrado para os filtros aplicados."
      />
    );
  }

  return <>{children}</>;
}

export function StudentsTableContent({
  students,
  renderActions,
}: Readonly<StudentsTableContentProps>) {
  return (
    <table className="table table-zebra w-full">
      <thead className="bg-base-200/90">
        <tr>
          <th className="app-th">Nome</th>
          <th className="app-th">CPF</th>
          <th className="app-th">Idade</th>
          <th className="app-th">Contato</th>
          <th className="app-th">Escola</th>
          <th className="app-th">Matricula</th>
          <th className="app-th">Status</th>
          {renderActions ? <th className="app-th">Acoes</th> : null}
        </tr>
      </thead>

      <tbody>
        {students.map((student) => (
          <tr
            key={student.id}
            className="transition-colors hover:bg-base-200/70"
          >
            <td className="app-td">{student.name}</td>
            <td className="app-td">{formatCpf(student.cpf)}</td>
            <td className="app-td text-center">{student.age}</td>
            <td className="app-td">{formatPhone(student.contact)}</td>
            <td className="app-td">{student.school}</td>
            <td className="app-td">{formatDateShortYear(student.createdAt)}</td>
            <td className="app-td">
              {student.archivedAt ? "Arquivado" : "Ativo"}
            </td>
            {renderActions ? (
              <td className="app-td">{renderActions(student)}</td>
            ) : null}
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export function StudentsTablePagination({
  currentPage,
  totalElements,
  totalPages,
  currentElementsCount,
  onPageChange,
  itemName = "alunos",
}: Readonly<StudentsTablePaginationProps>) {
  return (
    <Pagination
      currentPage={currentPage}
      totalElements={totalElements}
      totalPages={totalPages}
      currentElementsCount={currentElementsCount}
      itemName={itemName}
      onPageChange={onPageChange}
    />
  );
}
