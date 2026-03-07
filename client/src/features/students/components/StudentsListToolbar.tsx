import { ListPageHeader } from "@/components/ui/list-page-header"
import { Input } from "@/components/ui/input"
import styles from "@/features/students/components/StudentsListToolbar.module.css"

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
      <ListPageHeader title="Alunos" description="Gerencie cadastros e matrículas." actionLabel="Novo aluno" actionTo="/students/new" />

      <div className={styles.filters}>
        <div className={styles.filtersRow}>
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

          <label className={styles.checkboxField}>
            <input
              type="checkbox"
              checked={hideArchived}
              onChange={(event) => onHideArchivedChange(event.target.checked)}
            />
            Ocultar arquivados
          </label>
        </div>
        <div className={styles.meta}>Filtros: {activeFiltersLabel}</div>
      </div>
    </>
  )
}
