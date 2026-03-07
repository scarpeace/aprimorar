import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import styles from "@/features/students/pages/StudentsPage.module.css"
import { Link } from "react-router-dom"

type StudentsListToolbarProps = {
  hideArchived: boolean
  nameFilterInput: string
  activeFiltersLabel: string
  onHideArchivedChange: (checked: boolean) => void
  onNameFilterInputChange: (value: string) => void
}

export function StudentsListToolbar({
  hideArchived,
  nameFilterInput,
  activeFiltersLabel,
  onHideArchivedChange,
  onNameFilterInputChange,
}: StudentsListToolbarProps) {
  return (
    <>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Alunos</h1>
          <p className="text-sm text-gray-600">Gerencie cadastros e matrículas.</p>
          <label className="mt-3 flex items-center gap-2 text-sm text-gray-700">
            <input
              type="checkbox"
              checked={hideArchived}
              onChange={(event) => onHideArchivedChange(event.target.checked)}
            />
            Ocultar arquivados
          </label>
        </div>
        <Button asChild type="button">
          <Link to="/students/new">Novo aluno</Link>
        </Button>
      </div>

      <div className={styles.filters}>
        <div className={styles.filterField}>
          <label className={styles.filterLabel} htmlFor="student-name-filter">
            Buscar por nome
          </label>
          <Input
            id="student-name-filter"
            type="search"
            value={nameFilterInput}
            onChange={(event) => onNameFilterInputChange(event.target.value)}
            placeholder="Ex: Maria"
          />
        </div>
        <div className={styles.meta}>Filtros: {activeFiltersLabel}</div>
      </div>
    </>
  )
}
