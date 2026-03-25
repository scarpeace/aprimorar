import { useState } from "react"
import { GraduationCap } from "lucide-react"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { ButtonLink } from "@/components/ui/button"
import styles from "@/features/students/StudentsPage.module.css"
import { useDebounce } from "@/lib/shared/use-debounce"
import { StudentsTable } from "./components/StudentsTable"

export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("")
  const debouncedSearchTerm = useDebounce(searchTerm, 500)

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
      <StudentsTable variant="page" searchTerm={debouncedSearchTerm} />
    </div>
  )
}
