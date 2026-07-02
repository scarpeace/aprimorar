import { Button } from "@/components/ui/Button";

type TablePaginationProps = {
  summary: string;
  hasPrevious: boolean;
  hasNext: boolean;
  onPrevious: () => void;
  onNext: () => void;
};

export function TablePagination({
  summary,
  hasPrevious,
  hasNext,
  onPrevious,
  onNext,
}: Readonly<TablePaginationProps>) {
  return (
    <div className="flex flex-col gap-3 rounded-xl border border-base-300 bg-base-100 px-4 py-3 text-sm text-base-content/70 md:flex-row md:items-center md:justify-between">
      <p>{summary}</p>

      <div className="flex gap-2">
        <Button type="button" variant="outline" disabled={!hasPrevious} onClick={onPrevious}>
          Anterior
        </Button>
        <Button type="button" variant="outline" disabled={!hasNext} onClick={onNext}>
          Próxima
        </Button>
      </div>
    </div>
  );
}
