import { useState } from "react";
import { GraduationCap } from "lucide-react";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { ButtonLink } from "@/components/ui/button";
import styles from "@/features/students/StudentsPage.module.css";
import { useDebounce } from "@/lib/shared/use-debounce";
import { useStudents } from "./query/studentQueries";
import type { StudentResponseDTO } from "@/kubb/types/StudentResponseDTO";
import { TableRoot } from "@/components/layout/TableRoot";
import { StudentsTable } from "./components/StudentsTable/StudentsTable";


export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: studentsPage,
    isLoading: isStudentsPageLoading,
    error: studentsPageError,
  } = useStudents({ page: currentPage, size: 8, search: debouncedSearchTerm });

  return (
    <div className={styles.page}>
      <PageHeader
        description="Gerencie cadastros e matrículas."
        title="Alunos"
        Icon={GraduationCap}
        iconClassName="text-success"
        iconBgClassName="bg-success/15"
      >
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar aluno por nome, email ou escola"
            ariaLabel="Buscar aluno"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <ButtonLink
            className="sm:ml-auto"
            to="/students/new"
            variant="success"
          >
            Novo aluno
          </ButtonLink>
        </div>
      </PageHeader>


      <TableRoot>
        <StudentsTable.State students={studentsPage?.content} isLoading={isStudentsPageLoading} error={studentsPageError} >
          <StudentsTable.Content
            students={studentsPage?.content ?? []}
            renderActions={(student : StudentResponseDTO) => (
              <ButtonLink
                to={`/students/${student.id}`}
                size="sm"
                variant="outline"
              >
                Detalhes
              </ButtonLink>
            )}
          />
          <StudentsTable.Pagination
            currentPage={currentPage}
            onPageChange={setCurrentPage}
            totalElements={studentsPage?.page?.totalElements ?? 0}
            totalPages={studentsPage?.page?.totalPages ?? 0}
            currentElementsCount={studentsPage?.content?.length ?? 0}
          />
        </StudentsTable.State>
      </TableRoot>
    </div>
  );
}
