import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { useGetStudentsByParent } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";

interface ParentStudentsTableProps {
  parentId: string;
  currentPage: number;
  onPageChange: (page: number) => void;
}

export function ParentStudentsTable({
  parentId,
  currentPage,
  onPageChange,
}: Readonly<ParentStudentsTableProps>) {
  const navigate = useNavigate();
  const studentsQuery = useGetStudentsByParent(parentId, {
    page: currentPage,
    size: 10,
  });

  if (studentsQuery.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar os alunos vinculados"
        error={studentsQuery.error}
      />
    );
  }

  if (studentsQuery.isPending) {
    return (
      <div className="flex justify-center py-10">
        <LoadingSpinner text="Carregando alunos vinculados..." />
      </div>
    );
  }

  const students = studentsQuery.data;

  if (!students || students.content.length === 0) {
    return (
      <EmptyCard
        title="Nenhum aluno vinculado"
        description="Este responsável ainda não possui alunos associados."
      />
    );
  }

  return (
    <>
      <div className="overflow-x-auto">
        <table className="table table-zebra bg-base-100 shadow-2xl animate-[fade-up_280ms_ease-out_both]">
          <thead className="bg-base-300">
            <tr>
              <th className="text-left font-semibold text-base-content/80">Nome</th>
              <th className="text-left font-semibold text-base-content/80">CPF</th>
              <th className="text-left font-semibold text-base-content/80">Idade</th>
              <th className="text-left font-semibold text-base-content/80">Contato</th>
              <th className="text-left font-semibold text-base-content/80">Escola</th>
              <th className="text-left font-semibold text-base-content/80">Matrícula</th>
              <th className="text-left font-semibold text-base-content/80">Status</th>
            </tr>
          </thead>
          <tbody className="whitespace-nowrap">
            {students.content.map((student) => {
              const isArchived = student.active === false;

              return (
                <tr
                  key={student.id}
                  className="transition-colors hover:cursor-pointer hover:bg-base-200/70"
                  onClick={() => navigate(`/students/${student.id}`)}
                >
                  <td>{student.name}</td>
                  <td>{formatCpf(student.cpf)}</td>
                  <td className="text-center">{student.age}</td>
                  <td>{formatPhone(student.contact)}</td>
                  <td>{student.school}</td>
                  <td>{formatDateShortYear(student.createdAt)}</td>
                  <td>
                    <span className={`badge ${isArchived ? "badge-ghost" : "badge-success"} badge-sm`}>
                      {isArchived ? "Inativo" : "Ativo"}
                    </span>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      <Pagination
        paginationData={students}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
