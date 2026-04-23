import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOParentResponseDTO, ParentResponseDTO } from "@/kubb";
import { formatCpf, formatPhone } from "@/lib/utils/formatter";
import { SquareArrowOutUpRightIcon, Pencil } from "lucide-react";

type ParentsTableProps = {
  parents?: PageDTOParentResponseDTO;
  onPageChange: (page: number) => void;
  currentPage: number;
  isPending: boolean;
  error: unknown;
  onEdit: (parent: ParentResponseDTO) => void;
};

export function ParentsTable({
  parents,
  onPageChange,
  currentPage,
  isPending,
  error,
  onEdit,
}: Readonly<ParentsTableProps>) {
  if (error) {
    return (
      <ErrorCard
        title="Não foi possível carregar a listagem de Responsáveis"
        error={error}
      />
    );
  }

  if (isPending) {
    return <LoadingSpinner text="Carregando Responsáveis..." />;
  }

  return (
    <>
      <table className="table table-zebra shadow-2xl bg-base-100 animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-300">
          <tr>
            <th className="text-left font-semibold text-base-content/80">
              Nome
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Contato
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Email
            </th>
            <th className="text-left font-semibold text-base-content/80">
              CPF
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Status
            </th>
            <th className="text-right font-semibold text-base-content/80 pr-4">
              Ações
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {parents?.content.map((parent) => (
            <tr
              key={parent.parentId}
              className="transition-colors hover:bg-base-200/70"
            >
              <td>{parent.name}</td>

              <td>{formatPhone(parent.contact)}</td>
              <td>{parent.email}</td>
              <td>{formatCpf(parent.cpf)}</td>

              <td>{parent.archivedAt ? "Arquivado" : "Ativo"}</td>

              <td className="text-right flex justify-end gap-2 pr-2">
                <Button
                  className="btn-square btn-ghost btn-xs text-info"
                  onClick={() => onEdit(parent)}
                  title="Editar"
                >
                  <Pencil size={16} />
                </Button>
                <ButtonLink
                  className="btn btn-primary btn-xs btn-square"
                  to={`/parents/${parent.parentId}`}
                  title="Detalhes"
                >
                  <SquareArrowOutUpRightIcon className="h-4 w-4" />
                </ButtonLink>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <Pagination
        paginationData={parents}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
