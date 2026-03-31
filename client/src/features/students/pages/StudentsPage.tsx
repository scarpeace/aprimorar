import { ButtonLink } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { useGetStudents } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { GraduationCap } from "lucide-react";
import { useState } from "react";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { LoadingCard } from "@/components/ui/loading-card";
import { ErrorCard } from "@/components/ui/error-card";
import { Pagination } from "@/components/ui/pagination";
import { StudentsTable } from "../components/StudentsTable";

export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const { data: studentsResponse, isPending, error, } = useGetStudents({
    page: currentPage,
    size: 8,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  const { content: students, ...pageMetadata } = studentsResponse || {}

  return (
    <>
      <PageHeader
        description="Gerencie cadastros e matrículas."
        title="Alunos"
        Icon={GraduationCap}
      >
        <div className="flex items-center ml-auto gap-6">
          <ListSearchInput
            className="w-80 sm:w-96"
            placeholder="Buscar aluno por nome, email ou escola"
            ariaLabel="Buscar aluno"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <ToggleSwitch
            label="Arquivados"
            tip="Mostrar alunos arquivados"
            toggled={showArchived}
            setToggle={setShowArchived}
          />
          <ButtonLink to="/students/new" variant="success">
            Novo aluno
          </ButtonLink>
        </div>
      </PageHeader>

      <StudentsTable
        students={students}
         isPending={isPending}
          error={error}
      >
          <Pagination
            paginationData={pageMetadata}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
          />
      </StudentsTable>
    </>
  );
}
