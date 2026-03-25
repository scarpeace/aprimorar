import { useState } from "react";
import { ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { Pagination } from "@/components/ui/pagination";
import { useGetStudentsByParent } from "@/kubb";
import { PageLoading } from "@/components/ui/page-loading";
import { useStudents } from "../query/studentQueries";

export type StudentsTableVariant = "page" | "embedded";

//TODO: Essa tabela precisa de um refactor
type StudentsTableProps = {
  variant?: StudentsTableVariant;
  ownerId?: string;
  searchTerm?: string;
};

export function StudentsTable({
  variant = "page",
  ownerId,
  searchTerm = "",
}: Readonly<StudentsTableProps>) {
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 10;
  const {
    data: studentsPage,
    isLoading: studentsPageLoading,
    error: studentsPageError,
  } = useStudents({ page: currentPage, size: pageSize, search: searchTerm }); //TODO: tem que botar um debounce aqui

  console.log(studentsPage);
  // // Reset pagination when search changes
  // useEffect(() => {
  //   setCurrentPage((page) => (page === 0 ? page : 0));
  // }, [searchTerm]);

  const {
    data: studentsByParent,
    isLoading: studentsByParentLoading,
    error: studentsByParentError,
  } = useGetStudentsByParent(ownerId ?? "");

  const studentsList = () => {
    if (variant === "page") {
      return studentsPage?.content;
    }
    return studentsByParent;
  };

  if (studentsPageLoading || studentsByParentLoading) {
    return <PageLoading message="Carregando alunos..." />;
  }

  if (studentsPageError || studentsByParentError) {
    return (
      <ErrorCard
        description={
          studentsPageError?.message ||
          studentsByParentError?.message ||
          "Ocorreu um erro."
        }
        title="Erro ao carregar alunos"
      />
    );
  }

  return (
    <div className="app-table-wrap">
      <div className="overflow-x-auto">
        <table className="table table-zebra w-full">
          <thead className="bg-base-200/90">
            <tr>
              <th className="app-th">Nome</th>
              {variant === "page" && (
                <th className="app-th hidden lg:table-cell">Email</th>
              )}
              <th className="app-th">Escola</th>
              {variant === "embedded" && (
                <th className="app-th-center">Idade</th>
              )}
              {variant === "embedded" && <th className="app-th">Contato</th>}
              {variant === "page" && <th className="app-th">Responsável</th>}
              {variant === "page" && <th className="app-th-center">Status</th>}
              <th className="app-th">Ações</th>
            </tr>
          </thead>
          <tbody>
            {studentsList()?.map((student) => (
              <tr
                className="transition-colors hover:bg-base-200/70"
                key={student.id}
              >
                <td className="font-medium">{student.name}</td>
                {variant === "page" && (
                  <td className="hidden lg:table-cell">{student.email}</td>
                )}
                <td>{student.school}</td>
                {variant === "embedded" && (
                  <td className="text-center">{student.age}</td>
                )}
                {variant === "embedded" && <td>{student.contact}</td>}
                {variant === "page" && <td>{student?.parent?.name}</td>}
                {variant === "page" && (
                  <td className="text-center">
                    <span
                      className={`badge ${student.archivedAt ? "badge-warning" : "badge-success"}`}
                    >
                      {student.archivedAt ? "Arquivado" : "Ativo"}
                    </span>
                  </td>
                )}
                <td>
                  <ButtonLink
                    size="sm"
                    to={`/students/${student.id}`}
                    variant="outline"
                  >
                    Detalhes
                  </ButtonLink>
                </td>
              </tr>
            ))}
            {studentsPage?.content?.length === 0 && searchTerm && (
              <tr>
                <td
                  colSpan={variant === "page" ? 6 : 5}
                  className="text-center py-8 text-base-content/50"
                >
                  Nenhum aluno encontrado.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {variant === "page" && (
        <Pagination
          currentPage={currentPage}
          totalElements={studentsPage?.page?.totalElements ?? 0}
          totalPages={studentsPage?.page?.totalPages ?? 0}
          currentElementsCount={studentsPage?.content?.length ?? 0}
          itemName="alunos"
          onPageChange={setCurrentPage}
        />
      )}
    </div>
  );
}
