import { Button } from "@/components/ui/button"
import styles from "@/features/students/pages/StudentsPage.module.css"
import { STUDENT_PAGE_SIZE_OPTIONS } from "@/features/students/utils/studentListUtils"

type StudentsPaginationProps = {
  page: number
  size: number
  totalPages: number
  totalElements: number
  isFirstPage: boolean
  isLastPage: boolean
  onPageChange: (updater: (currentPage: number) => number) => void
  onSizeChange: (size: number) => void
}

export function StudentsPagination({
  page,
  size,
  totalPages,
  totalElements,
  isFirstPage,
  isLastPage,
  onPageChange,
  onSizeChange,
}: StudentsPaginationProps) {
  if (totalElements === 0) {
    return null
  }

  return (
    <div className={styles.pagination}>
      <div className={styles.meta}>
        Página {page + 1} de {Math.max(totalPages, 1)} • {totalElements} aluno(s)
      </div>
      <div className={styles.paginationActions}>
        <label className={styles.pageSizeLabel} htmlFor="students-page-size">
          Itens por página
        </label>
        <select
          id="students-page-size"
          className={styles.pageSizeSelect}
          value={size}
          onChange={(event) => onSizeChange(Number(event.target.value))}
        >
          {STUDENT_PAGE_SIZE_OPTIONS.map((option) => (
            <option key={option} value={option}>
              {option}
            </option>
          ))}
        </select>
        <Button type="button" variant="outline" onClick={() => onPageChange((prev) => prev - 1)} disabled={isFirstPage}>
          Anterior
        </Button>
        <Button type="button" variant="outline" onClick={() => onPageChange((prev) => prev + 1)} disabled={isLastPage}>
          Próxima
        </Button>
      </div>
    </div>
  )
}
