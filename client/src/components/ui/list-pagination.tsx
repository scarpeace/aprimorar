import { Button } from "@/components/ui/button"

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
    <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
      <div className="text-xs text-muted-foreground">
        Página {page + 1} de {Math.max(totalPages, 1)} • {summaryLabel}
      </div>
      <div className="flex flex-wrap items-center gap-2">
        <label className="text-xs text-muted-foreground" htmlFor={selectId}>
          Itens por página
        </label>
        <select
          id={selectId}
          className="h-10 min-w-[4.5rem] rounded-md border border-input bg-background px-2 text-sm"
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
