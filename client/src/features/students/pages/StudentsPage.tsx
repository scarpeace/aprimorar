import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetStudents } from "@/kubb";
import type { StudentResponseDTO } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { GraduationCap, Plus } from "lucide-react";
import { useState } from "react";
import { StudentsTable } from "../components/StudentsTable";
import { StudentForm } from "../components/StudentForm";

export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState<StudentResponseDTO | null>(null);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const studentsQuery = useGetStudents({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  const headerProps = {
    description: "Gerencie cadastros e matrículas.",
    title: "Alunos",
    Icon: GraduationCap,
    backLink: "/",
  };

  const handleOpenForm = (student?: StudentResponseDTO) => {
    setSelectedStudent(student || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedStudent(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex flex-col gap-3 w-full">
        <div className="flex flex-col sm:flex-row gap-3">
          <ListSearchInput
            className="grow sm:mr-3"
            placeholder="Buscar aluno por nome, email ou CPF"
            ariaLabel="Buscar aluno"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <div className="flex justify-between sm:justify-end w-full sm:w-auto items-center gap-3">
            <ToggleSwitch
              label="Arquivados"
              tip="Mostrar alunos arquivados"
              toggled={showArchived}
              setToggle={setShowArchived}
            />
            <Button
              className="sm:ml-auto"
              onClick={() => handleOpenForm()}
              variant="success"
            >
              <Plus className="h-4 w-4 mr-2" />
              Novo Aluno
            </Button>
          </div>
        </div>

        <StudentsTable
          students={studentsQuery.data}
          onPageChange={setCurrentPage}
          currentPage={currentPage}
          isPending={studentsQuery.isPending}
          error={studentsQuery.error}
          onEdit={(student) => handleOpenForm(student)}
        />

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-4xl">
              <h3 className="font-bold text-lg mb-4">
                {selectedStudent ? "Editar Aluno" : "Cadastrar Novo Aluno"}
              </h3>
              <StudentForm
                initialData={selectedStudent}
                onSuccess={handleCloseForm}
                onCancel={handleCloseForm}
              />
            </div>
          </div>
        )}
      </div>
    </PageLayout>
  );
}
