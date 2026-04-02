import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOStudentResponseDTO } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import type { ReactNode } from "react";
import { useNavigate } from "react-router-dom";

type StudentsTableProps = {
  students?: PageDTOStudentResponseDTO;
  children?: ReactNode;
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

  if (isPending) {
    return <LoadingSpinner text="Carregando Alunos..." />;
  }

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Alunos" error={error} />;
  }

  return (
    <>
      <div>
        <table className="table table-zebra bg-base-100 overflow-x-auto w-full p-3 rounded-xl animate-[fade-up_280ms_ease-out_both]">
          <thead className="bg-base-300 rounded">
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
              <th className="text-left font-semibold text-base-content/80">
                Status
              </th>
              <th className="text-left font-semibold text-base-content/80">
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
                  <span
                    className="btn m-2 btn-secondary"
                    onClick={() => navigate(`/students/${student.id}`)}
                  >
                    Detalhes
                  </span>
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
      </div>
    </>
  );
}
