//TODO: voltar aqui depois e ver esse content unknown
export type PaginationProps = {
  paginationData?: {
    size: number;
    totalElements: number;
    totalPages: number;
    content:unknown[]
  };
  currentPage: number;
  onPageChange: (page: number) => void;
};

export function Pagination({
  currentPage,
  onPageChange,
  paginationData,
}: Readonly<PaginationProps>) {

  if (!paginationData) return null;

  return (
    <div className="mt-4 flex items-center justify-between px-3">
      <p className="text-sm app-text-muted hidden lg:block">
        Mostrando {paginationData.content.length} de {paginationData.totalElements}
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
