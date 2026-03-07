import { Button } from "@/components/ui/button"
import styles from "@/components/ui/list-pagination.module.css"

type ListPaginationProps = {
  page: number
  size: number
  totalPages: number
  totalElements: number
  isFirstPage: boolean
  isLastPage: boolean
  summaryLabel: string
  pageSizeOptions?: readonly number[]
  selectId: string
  onPageChange: (updater: (currentPage: number) => number) => void
  onSizeChange: (size: number) => void
}

const DEFAULT_PAGE_SIZE_OPTIONS = [10, 20, 50] as const

export function ListPagination({
  page,
  size,
  totalPages,
  totalElements,
  isFirstPage,
  isLastPage,
  summaryLabel,
  pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS,
  selectId,
  onPageChange,
  onSizeChange,
}: ListPaginationProps) {
  if (totalElements === 0) {
    return null
  }

  return (
    <div className={styles.pagination}>
      <div className={styles.summary}>
        Página {page + 1} de {Math.max(totalPages, 1)} • {summaryLabel}
      </div>
      <div className={styles.actions}>
        <label className={styles.label} htmlFor={selectId}>
          Itens por página
        </label>
        <select
          id={selectId}
          className={styles.select}
          value={size}
          onChange={(event) => onSizeChange(Number(event.target.value))}
        >
          {pageSizeOptions.map((option) => (
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
