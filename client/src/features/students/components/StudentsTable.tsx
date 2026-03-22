import { useState, useEffect } from "react";
import { ButtonLink } from "@/components/ui/button";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { Pagination } from "@/components/ui/pagination";
import type { StudentResponse } from "@/features/students/schemas/student";
import { useGetStudents } from "@/gen";
import { useStudentsByParentQuery } from "../query/useStudentQueries";

export type StudentsTableVariant = "studentsPage" | "embeddedParent";

//TODO: Essa tabela precisa de um refactor
type StudentsTableProps = {
  variant?: StudentsTableVariant;
  ownerId?: string;
  searchTerm?: string;
};

export function StudentsTable({
  variant = "studentsPage",
  ownerId,
  searchTerm = "",
}: Readonly<StudentsTableProps>) {
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 10;

  // Reset pagination when search changes
  useEffect(() => {
    setCurrentPage(0);
  }, [searchTerm]);

  const studentsPageResults = useGetStudents({
    pageable: {
      page: currentPage,
      size: pageSize,
      sort: ["name"],
    },
    search: searchTerm,
  });

  const parentStudentsResults = useStudentsByParentQuery(ownerId ?? "", {
    enabled: variant === "embeddedParent" && !!ownerId,
  });

  const isLoading =
    variant === "studentsPage"
      ? studentsPageResults.isLoading
      : parentStudentsResults.isLoading;
  const error =
    variant === "studentsPage"
      ? studentsPageResults.error
      : parentStudentsResults.error;
  const data =
    variant === "studentsPage"
      ? studentsPageResults.data
      : parentStudentsResults.data;

  const studentList: StudentResponse[] = Array.isArray(data)
    ? data
    : (data?.content ?? []);
  const pageInfo = Array.isArray(data) ? undefined : data?.page;

  if (isLoading) {
    return <LoadingCard description="Carregando alunos..." />;
  }

  const totalElements = pageInfo?.totalElements ?? studentList.length;

  if (totalElements === 0 && !searchTerm) {
    return (
      <EmptyCard
        description="Cadastre um aluno para ele aparecer por aqui."
        title={"Nenhum aluno encontrado."}
      />
    );
  }

  if (error) {
    return (
      <ErrorCard
        description={error.message || "Ocorreu um erro."}
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
              {variant === "studentsPage" && (
                <th className="app-th hidden lg:table-cell">Email</th>
              )}
              <th className="app-th">Escola</th>
              {variant === "embeddedParent" && (
                <th className="app-th-center">Idade</th>
              )}
              {variant === "embeddedParent" && (
                <th className="app-th">Contato</th>
              )}
              {variant === "studentsPage" && (
                <th className="app-th">Responsável</th>
              )}
              {variant === "studentsPage" && (
                <th className="app-th-center">Status</th>
              )}
              <th className="app-th">Ações</th>
            </tr>
          </thead>
          <tbody>
            {studentList.map((student) => (
              <tr
                className="transition-colors hover:bg-base-200/70"
                key={student.id}
              >
                <td className="font-medium">{student.name}</td>
                {variant === "studentsPage" && (
                  <td className="hidden lg:table-cell">{student.email}</td>
                )}
                <td>{student.school}</td>
                {variant === "embeddedParent" && (
                  <td className="text-center">{student.age}</td>
                )}
                {variant === "embeddedParent" && <td>{student.contact}</td>}
                {variant === "studentsPage" && <td>{student?.parent?.name}</td>}
                {variant === "studentsPage" && (
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
            {studentList.length === 0 && searchTerm && (
              <tr>
                <td
                  colSpan={variant === "studentsPage" ? 6 : 5}
                  className="text-center py-8 text-base-content/50"
                >
                  Nenhum aluno encontrado.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {variant === "studentsPage" && (
        <Pagination
          currentPage={currentPage}
          totalElements={pageInfo?.totalElements ?? 0}
          totalPages={pageInfo?.totalPages ?? 0}
          currentElementsCount={studentList.length}
          itemName="alunos"
          onPageChange={setCurrentPage}
        />
      )}
    </div>
  );
}
