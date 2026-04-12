import { ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOStudentResponseDTO } from "@/kubb";
import {
    formatCpf,
    formatDateShortYear,
    formatPhone,
} from "@/lib/utils/formatter";
import { SquareArrowOutUpRightIcon } from "lucide-react";

type StudentsTableProps = {
  students?: PageDTOStudentResponseDTO;
  onPageChange: (page: number) => void;
  currentPage: number;
  isPending: boolean;
  error: unknown;
};

export function StudentsTable({
  students,
  onPageChange,
  currentPage,
  isPending,
  error,
}: Readonly<StudentsTableProps>) {
  if (error) {
    return (
      <ErrorCard
        title="Não foi possível carregar a listagem de Alunos"
        error={error}
      />
    );
  }

  if (isPending) {
    return <LoadingSpinner text="Carregando Alunos..." />;
  }

  return (
    <>
      <table className="table table-zebra shadow-2xl bg-base-100 animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-300 ">
          <tr>
            <th className="text-left font-semibold text-base-content/80">
              Nome
            </th>
            <th className="text-left font-semibold text-base-content/80">
              CPF
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Idade
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Contato
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Escola
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Matricula
            </th>
            {/*TODO: Trocar esse status por uma bolinha de status e mover as actions pra dentro da row*/}
            <th className="text-left font-semibold text-base-content/80">
              Status
            </th>
            <th className="text-center font-semibold text-base-content/80">
              Ações
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {students?.content.map((student) => (
            <tr
              key={student.id}
              className="transition-colors hover:bg-base-200/70"
            >
              <td>{student.name}</td>

              <td>{formatCpf(student.cpf)}</td>
              <td className="text-center">{student.age}</td>
              <td>{formatPhone(student.contact)}</td>

              <td>{student.school}</td>

              <td>{formatDateShortYear(student.createdAt ?? "")}</td>
              <td>{student.archivedAt ? "Arquivado" : "Ativo"}</td>

              <td>
                <ButtonLink
                  className="btn btn-primary btn-ou btn-square"
                  to={`/students/${student.id}`}
                >
                  <SquareArrowOutUpRightIcon className="h-4 w-4" />
                </ButtonLink>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <Pagination
        paginationData={students}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
