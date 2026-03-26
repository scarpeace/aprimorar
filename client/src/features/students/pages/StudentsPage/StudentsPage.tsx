import { useState } from "react";
import { Check, ChevronDown, ChevronUpCircle, GraduationCap, PersonStanding } from "lucide-react";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { ButtonLink } from "@/components/ui/button";
import styles from "./students-page.module.css";
import { useDebounce } from "@/lib/shared/use-debounce";
import { TableRoot } from "@/components/layout/TableRoot";
import { StudentsTable } from "./components/StudentsTable";
import { useGetStudents, type StudentResponseDTO } from "@/kubb";
import path from "path";

export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [expandData, setExpandData] = useState(false);

  const {
    data: studentsPage,
    isLoading: isStudentsPageLoading,
    error: studentsPageError,
  } = useGetStudents({
    page: currentPage,
    size: 8,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

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

          <div className="flex gap-2 justify-between items-center px-2">
            <div className="tooltip" data-tip="Mostrar Arquivados">
              <label id="showArchived" className="label mx-auto">
                <input
                  id="showArchived"
                  type="checkbox"
                  checked={showArchived}
                  onChange={() => setShowArchived(!showArchived)}
                  className="toggle checked:border-orange-500 checked:bg-orange-400 checked:text-orange-800"
                />
              </label>
            </div>

            <div className="tooltip tooltip-right" data-tip="Ampliar Dados">
              <label id="expandData" className="label mx-auto">
                <input
                  id="expandData"
                  type="checkbox"
                  checked={expandData}
                  onChange={() => setExpandData(!expandData)}
                  className="toggle checked:border-orange-500 checked:bg-orange-400 checked:text-orange-800"
                />
              </label>
            </div>
          </div>

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
        <StudentsTable.State
          students={studentsPage?.content}
          isLoading={isStudentsPageLoading}
          error={studentsPageError}
        >
          <StudentsTable.Content
            students={studentsPage?.content ?? []}
            renderActions={(student: StudentResponseDTO) => (
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
