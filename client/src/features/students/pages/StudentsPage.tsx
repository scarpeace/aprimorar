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
  const params = { page: currentPage, search: debouncedSearchTerm, archived: showArchived };

  const {
    isPending: isStudentsPending,
    data: students,
    error: studentsError,
  } = useGetStudents(params);

  return (
    <>
      <PageHeader
        description="Gerencie cadastros e matrículas."
        title="Alunos"
        Icon={GraduationCap}
        backLink="/dashboard"
      />
      <div className="flex items-center justify-between ml-auto">
        <div className="flex flex-1 items-center gap-2">
          <ListSearchInput
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
        </div>
        <ButtonLink to="/students/new" variant="success">
          Novo Aluno
        </ButtonLink>
      </div>

      <StudentsTable
        students={students}
        onPageChange={setCurrentPage}
        currentPage={currentPage}
        isPending={isStudentsPending}
        error={studentsError}
      />
    </>
  );
}
