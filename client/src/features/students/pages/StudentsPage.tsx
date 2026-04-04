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
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const {
    data: students,
    isPending,
    error,
  } = useGetStudents({
    page: currentPage,
    size: 8,
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
      <div className="flex items-center justify-between ml-auto gap-6">
        <ListSearchInput
          className="grow"
          placeholder="Buscar aluno por nome, email ou escola"
          ariaLabel="Buscar aluno"
          value={searchTerm}
          onChange={setSearchTerm}
        />
        <ToggleSwitch
          className="justify-self-start"
          label="Arquivados"
          tip="Mostrar alunos arquivados"
          toggled={showArchived}
          setToggle={setShowArchived}
        />
        <ButtonLink to="/students/new" variant="success">
          Novo aluno
        </ButtonLink>
      </div>
      <StudentsTable
        students={students}
        onPageChange={setCurrentPage}
        currentPage={currentPage}
        isPending={isPending}
        error={error}
      />
    </>
  );
}
