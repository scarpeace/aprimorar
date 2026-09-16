import { ChevronLeft, ChevronRight } from "lucide-react";

type PaginationProps = {
  currentPage: number;
  totalPages: number;
  onPrevious: () => void;
  onNext: () => void;
};

export function Pagination({
  currentPage,
  totalPages,
  onPrevious,
  onNext,
}: Readonly<PaginationProps>) {
  if (totalPages <= 1) {
    return null;
  }

  return (
    <div className="flex items-center gap-2">
      <button
        type="button"
        className="btn btn-square btn-xs border border-base-300/60 bg-base-100"
        disabled={currentPage === 0}
        onClick={onPrevious}
        aria-label="Página anterior"
      >
        <ChevronLeft size={14} />
      </button>

      <span className="px-2 text-xs whitespace-nowrap text-base-content/70">
        Página {currentPage + 1} de {totalPages}
      </span>

      <button
        type="button"
        className="btn btn-square btn-xs border border-base-300/60 bg-base-100"
        disabled={currentPage >= totalPages - 1}
        onClick={onNext}
        aria-label="Próxima página"
      >
        <ChevronRight size={14} />
      </button>
    </div>
  );
}
