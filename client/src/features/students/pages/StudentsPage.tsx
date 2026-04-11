import { ButtonLink } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetStudents } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { GraduationCap } from "lucide-react";
import { useState } from "react";
import { StudentsTable } from "../components/StudentsTable";

export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const studentsQuery = useGetStudents({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  return (
    <>
      <PageHeader
        description="Gerencie cadastros e matrículas."
        title="Alunos"
        Icon={GraduationCap}
        backLink="/dashboard"
      />
      <div className="flex flex-col gap-3 w-full">
        <div className="flex flex-row">
          <ListSearchInput
            className="grow sm:mr-3"
            placeholder="Buscar aluno por nome, email ou CPF"
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
          <ButtonLink className="sm:ml-auto" to="/students/new" variant="success">
            Novo Aluno
          </ButtonLink>
        </div>

        <StudentsTable
          students={studentsQuery.data}
          onPageChange={setCurrentPage}
          currentPage={currentPage}
          isPending={studentsQuery.isPending}
          error={studentsQuery.error}
        />
      </div>
    </>
  );
}
