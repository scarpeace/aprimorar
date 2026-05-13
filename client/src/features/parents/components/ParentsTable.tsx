import { Button } from "@/components/ui/button";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOParentResponseDTO, ParentResponseDTO } from "@/kubb";
import { formatCpf, formatPhone } from "@/lib/utils/formatter";
import { Pencil } from "lucide-react";
import { useNavigate } from "react-router-dom";

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
  const navigate = useNavigate();

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

  if (!parents || parents.content.length === 0) {
    return (
      <EmptyCard
        title="Nenhum responsavel encontrado"
        description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
      />
    );
  }

  return (
    <>
      <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-200/80">
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
            <th className="text-right font-semibold text-base-content/80">
              Acoes
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {parents?.content.map((parent) => (
            <tr
              key={parent.parentId}
              className="transition-colors hover:bg-base-300/70 hover:cursor-pointer"
              onClick={() => navigate(`/parents/${parent.parentId}`)}
            >
              <td>{parent.name}</td>

              <td>{formatPhone(parent.contact)}</td>
              <td>{parent.email}</td>
              <td>{formatCpf(parent.cpf)}</td>
              <td>
                <span className={`badge ${(parent.active ?? true) ? "badge-success" : "badge-ghost"} badge-sm`}>
                  {(parent.active ?? true) ? "Ativo" : "Arquivado"}
                </span>
              </td>
              <td className="text-right">
                <Button
                  type="button"
                  size="sm"
                  variant="outline"
                  onClick={(event) => {
                    event.stopPropagation();
                    onEdit(parent);
                  }}
                >
                  <Pencil className="h-4 w-4" />
                  Editar
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      </div>

      <Pagination
        paginationData={parents}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
