import { ChevronLeft, ChevronRight } from "lucide-react";
import type { ReactNode } from "react";

type PaginationProps = {
  currentPage: number;
  totalPages: number;
  summary?: ReactNode;
  onPrevious: () => void;
  onNext: () => void;
};

export function Pagination({
  currentPage,
  totalPages,
  summary,
  onPrevious,
  onNext,
}: Readonly<PaginationProps>) {
  return (
    <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
      {summary ? <p className="text-sm text-base-content/70">{summary}</p> : <span />}

      {totalPages > 1 ? (
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

          <span className="whitespace-nowrap px-2 text-xs text-base-content/70">
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
      ) : null}
    </div>
  );
}
