import { useState } from "react"
import { GraduationCap } from "lucide-react"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { ButtonLink } from "@/components/ui/button"
import styles from "@/features/students/StudentsPage.module.css"
import { useDebounce } from "@/lib/shared/use-debounce"
import { StudentTable } from "./components/StudentTable"
import { useStudents } from "./query/studentQueries"

export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("")
  const debouncedSearchTerm = useDebounce(searchTerm, 500)
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: students,
    isLoading: isStudentsLoading,
    error: studentsError
  } = useStudents({page: currentPage, size:8, search: debouncedSearchTerm})

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
          <ButtonLink className="sm:ml-auto" to="/students/new" variant="success">
            Novo aluno
          </ButtonLink>
        </div>
      </PageHeader>

      {/*TODO: substituir a tabela pela genérica*/}
      <StudentTable
        variant="page"
        data={students}
        isLoading={isStudentsLoading}
        error={studentsError ?? null}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
        itemName="alunos"
        renderActions={(student) => (
          <ButtonLink to={`/students/${student.id}`} size="sm" variant="outline">
            Detalhes
          </ButtonLink>
        )}
      />
    </div>
  )
}
