import type { PageMetadata } from "@/kubb"

export type PaginationProps = {
  totalElements: number
  totalPages: number
  currentPage: number
  onPageChange: (page: number) => void
}

export function Pagination({
  totalElements,
  totalPages,
  currentPage,
  onPageChange,
}: Readonly<PaginationProps>) {

  if (totalPages <= 1) return null;

  return (
    <div className="mt-4 flex items-center justify-between px-3">
      <p className="text-sm app-text-muted hidden lg:block">
        Mostrando {currentPage + 1} de {totalPages}
      </p>
      <div className="join mx-auto lg:mx-0 mb-3">
        <button
          className="join-item btn btn-sm"
          disabled={currentPage === 0}
          onClick={() => onPageChange(currentPage - 1)}
        >
          Anterior
        </button>
        <button className="btn btn-sm join-item no-animation cursor-default">
          Página {currentPage + 1} de {totalPages}
        </button>
        <button
          className="btn btn-sm join-item"
          disabled={currentPage >= totalPages - 1}
          onClick={() => onPageChange(currentPage + 1)}
        >
          Próxima
        </button>
      </div>
    </div>
  )
}
