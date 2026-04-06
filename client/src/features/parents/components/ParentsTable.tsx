import { ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOParentResponseDTO } from "@/kubb";
import {
  formatCpf,
  formatPhone
} from "@/lib/utils/formatter";
import type { ReactNode } from "react";

type ParentsTableProps = {
  parents?: PageDTOParentResponseDTO;
  children?: ReactNode;
  onPageChange: (page: number) => void;
  currentPage: number;
  isPending: boolean;
  error: unknown;
};

export function ParentsTable({
  parents,
  onPageChange,
  currentPage,
  isPending,
  error,
}: Readonly<ParentsTableProps>) {

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Responsáveis" error={error}/>;
  }

  if (isPending) {
    return <LoadingSpinner text="Carregando Responsáveis..." />;
  }

  return (
    <>
      <div className=" rounded bg-base-100 mt-3">
        <table className="table table-auto table-zebra animate-[fade-up_280ms_ease-out_both]">
          <thead className="bg-base-300 rounded">
            <tr>
              <th className="text-left font-semibold text-base-content/80">
                Nome
              </th>
              <th className="text-left font-semibold text-base-content/80">
                Email
              </th>
              <th className="text-left font-semibold text-base-content/80">
                Contato
              </th>
              <th className="text-left font-semibold text-base-content/80">
                CPF
              </th>
              <th className="text-left font-semibold text-base-content/80">
                Status
              </th>
              <th className="text-left font-semibold text-base-content/80">
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

                <td>{formatCpf(parent.cpf)}</td>
                <td>{formatPhone(parent.contact)}</td>
                <td>{parent.email}</td>

                <td>{parent.archivedAt ? "Arquivado" : "Ativo"}</td>

                <td>
                  <ButtonLink
                    className="btn btn-outline btn-info"
                    to={`/employees/${parent.parentId}`}
                  >
                    Detalhes
                  </ButtonLink>
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
