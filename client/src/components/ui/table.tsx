import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { Pagination } from "@/components/ui/pagination";
import type { ReactNode } from "react";

type PaginatedData<T> = {
  content?: T[];
  page?: {
    size?: number;
    totalElements?: number;
    totalPages?: number;
    number?: number;
  };
};

export type ColumnDef<T> = {
  header: string;
  accessor: (row: T) => ReactNode;
  headerClassName?: string;
  cellClassName?: string;
};

type TableProps<T> = {
  variant?: "page" | "embedded";
  data?: PaginatedData<T> | T[];
  columns: ColumnDef<T>[];
  isLoading: boolean;
  error: unknown;
  currentPage: number;
  onPageChange: (page: number) => void;
  itemName?: string;
  getRowKey: (row: T, index: number) => string;
  renderActions?: (row: T) => ReactNode;
  emptyTitle?: string;
  emptyDescription?: string;
  loadingDescription?: string;
  errorTitle?: string;
};

export function Table<T>({
  variant = "page",
  data,
  columns,
  isLoading,
  error,
  currentPage,
  onPageChange,
  itemName = "itens",
  getRowKey,
  renderActions,
  emptyTitle = "Nenhum dado encontrado.",
  emptyDescription = "Nenhum dado encontrado.",
  loadingDescription = "Carregando dados...",
  errorTitle = "Erro ao carregar dados",
}: Readonly<TableProps<T>>) {
  if (isLoading) {
    return <LoadingCard description={loadingDescription} />;
  }

  if (error) {
    return (
      <ErrorCard
        description={getFriendlyErrorMessage(error)}
        title={errorTitle}
      />
    );
  }

  const rows = Array.isArray(data) ? data : data?.content ?? [];
  const totalElements = Array.isArray(data)
    ? data.length
    : data?.page?.totalElements ?? rows.length;
  const totalPages = Array.isArray(data)
    ? (data.length > 0 ? 1 : 0)
    : data?.page?.totalPages ?? 0;

  if (totalElements === 0) {
    return <EmptyCard title={emptyTitle} description={emptyDescription} />;
  }

  return (
    <div className={variant === "page" ? "app-table-wrap" : ""}>
      <div className="overflow-x-auto">
        <table className="table table-zebra w-full">
          <thead className="bg-base-200/90">
            <tr>
              {columns.map((column) => (
                <th
                  key={column.header}
                  className={`app-th ${column.headerClassName ?? ""}`.trim()}
                >
                  {column.header}
                </th>
              ))}
              {renderActions ? <th className="app-th">Ações</th> : null}
            </tr>
          </thead>

          <tbody>
            {rows.map((row, index) => (
              <tr
                key={getRowKey(row, index)}
                className="transition-colors hover:bg-base-200/70"
              >
                {columns.map((column) => (
                  <td
                    key={column.header}
                    className={`app-td ${column.cellClassName ?? ""}`.trim()}
                  >
                    {column.accessor(row)}
                  </td>
                ))}
                {renderActions ? (
                  <td className="app-td">{renderActions(row)}</td>
                ) : null}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {variant === "page" ? (
        <Pagination
          currentPage={currentPage}
          totalElements={totalElements}
          totalPages={totalPages}
          currentElementsCount={rows.length}
          itemName={itemName}
          onPageChange={onPageChange}
        />
      ) : null}
    </div>
  );
}
