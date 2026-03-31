import type { PageMetadata } from "@/kubb";

export type PaginationProps = {
  paginationData?: PageMetadata
  currentPage: number;
  currentSize?: number;
  onPageChange: (page: number) => void;
};

export function Pagination({
  currentPage,
  onPageChange,
  paginationData,
  currentSize,
}: Readonly<PaginationProps>) {

  if (!paginationData) return null;

  return (
    <div className="mt-4 flex items-center justify-between px-3">
      <p className="text-sm app-text-muted hidden lg:block">
        Mostrando {currentSize} de {paginationData.totalElements}
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
          Página {currentPage + 1} de {paginationData.totalPages}
        </button>
        <button
          className="btn btn-sm join-item"
          disabled={!paginationData.totalPages || currentPage >= paginationData.totalPages - 1}
          onClick={() => onPageChange(currentPage + 1)}
        >
          Próxima
        </button>
      </div>
    </div>
  );
}
