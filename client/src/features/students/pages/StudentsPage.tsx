import { TableRoot } from "@/components/layout/TableRoot";
import { ButtonLink } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { useGetStudents } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { GraduationCap } from "lucide-react";
import { useState } from "react";
import { StudentsTable } from "../components/StudentsTable";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { LoadingCard } from "@/components/ui/loading-card";
import { ErrorCard } from "@/components/ui/error-card";
import { Pagination } from "@/components/ui/pagination";

export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const studentsQuery = useGetStudents({
    page: currentPage,
    size: 8,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  if (studentsQuery.isPending) {
    return <LoadingCard title="Carregando listagem de alunos" />;
  }

  if (studentsQuery.isError) {
    return (
      <ErrorCard
        title="Não foi possível carregar a listagem de alunos"
        error={studentsQuery.error}
      />
    );
  }

  return (
    <div className=" flex flex-col gap-5">
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
          <ButtonLink
            className="sm:ml-auto"
            to="/students/new"
            variant="success"
          >
            Novo aluno
          </ButtonLink>
        </div>
      </PageHeader>
      <TableRoot className="animate-[fade-up_280ms_ease-out_both] flex flex-col gap-7">
        <StudentsTable.State
          students={studentsQuery.data.content}
          isLoading={studentsQuery.isLoading}
          error={studentsQuery.error}
        >
          <StudentsTable.Content students={studentsQuery.data.content} />

          <Pagination
            totalElements={studentsQuery.data.totalElements}
            totalPages={studentsQuery.data.totalPages}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
          />
        </StudentsTable.State>
      </TableRoot>
    </div>
  );
}
