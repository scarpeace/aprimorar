import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOStudentResponseDTO } from "@/kubb";
import {
    formatCpf,
    formatDateShortYear,
    formatPhone,
} from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";

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
  const navigate = useNavigate();

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Alunos" error={error}/>;
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
              Responsável
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
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {students?.content.map((student) => (
            <tr
              key={student.id}
              className={`transition-colors hover:bg-base-200/70 hover:cursor-pointer ${student.archivedAt ? "text-gray-400" : ""}`}
              onClick={() => navigate(`/students/${student.id}`)}
            >
              <td>{student.name}</td>
              <td>{student.responsible?.name || "-"}</td>

              <td>{formatCpf(student.cpf)}</td>
              <td className="text-center">{student.age}</td>
              <td>{formatPhone(student.contact)}</td>

              <td>{student.school}</td>

              <td>{formatDateShortYear(student.createdAt ?? "")}</td>
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
