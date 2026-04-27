import { Button } from "./button";

export type PaginationProps<T = any> = {
  paginationData?: {
    size: number;
    totalElements: number;
    totalPages: number;
    content: T[];
  };
  currentPage: number;
  onPageChange: (page: number) => void;
};

export function Pagination<T>({
  currentPage,
  onPageChange,
  paginationData,
}: Readonly<PaginationProps<T>>) {

  if (!paginationData || paginationData.totalPages <= 1) return null;

  return (
    <div className="mt-6 flex flex-col sm:flex-row items-center justify-between gap-4 px-2">
      <p className="text-sm text-base-content/60">
        Mostrando <span className="font-semibold text-base-content">{paginationData.content.length}</span> de <span className="font-semibold text-base-content">{paginationData.totalElements}</span> registros
      </p>
      
      <div className="join shadow-sm border border-base-300">
        <Button
          variant="ghost"
          className="join-item btn-sm"
          disabled={currentPage === 0}
          onClick={() => onPageChange(currentPage - 1)}
        >
          Anterior
        </Button>
        
        <div className="join-item btn btn-sm btn-ghost no-animation cursor-default border-x border-base-300 bg-base-200/30">
          Página {currentPage + 1} de {paginationData.totalPages}
        </div>
        
        <Button
          variant="ghost"
          className="join-item btn-sm"
          disabled={currentPage >= paginationData.totalPages - 1}
          onClick={() => onPageChange(currentPage + 1)}
        >
          Próxima
        </Button>
      </div>
    </div>
  );
}
