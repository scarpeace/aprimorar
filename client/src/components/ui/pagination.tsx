

export type PaginationProps = {
  currentPage: number
  totalElements: number
  totalPages: number
  currentElementsCount: number
  itemName: string
  onPageChange: (page: number) => void
}

export function Pagination({
  currentPage,
  totalElements,
  totalPages,
  currentElementsCount,
  itemName = "itens",
  onPageChange,
}: Readonly<PaginationProps>) {
  if (totalPages <= 1) return null

  return (
    <div className="mt-4 flex items-center justify-between px-3">
      <p className="text-sm app-text-muted hidden lg:block">
        Mostrando {currentElementsCount} de {totalElements} {itemName}
      </p>
      <div className="join mx-auto mb-3">
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
