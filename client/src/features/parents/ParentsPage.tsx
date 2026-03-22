import { useState, useEffect } from "react";
import { Handshake } from "lucide-react";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { PageLoading } from "@/components/ui/page-loading";
import { Pagination } from "@/components/ui/pagination";
import { ButtonLink } from "@/components/ui/button";
import styles from "@/features/parents/ParentsPage.module.css";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useDebounce } from "@/lib/shared/use-debounce";
import { useGetParents } from "@/gen";

export function ParentsPage() {
  const [currentPage, setCurrentPage] = useState(0);
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const pageSize = 10;

  const {
    data: parentsPage,
    isLoading,
    error,
    refetch,
  } = useGetParents({
    pageable: {
      page: currentPage,
      size: pageSize,
      sort: ["name"]
    },
  });

  // Reset pagination when search changes
  useEffect(() => {
    setCurrentPage(0);
  }, [debouncedSearchTerm]);

  return (
    <div className={styles.page}>
      <PageHeader
        description="Gerencie pais e responsáveis."
        title="Responsáveis"
        Icon={Handshake}
        iconClassName="text-error"
        iconBgClassName="bg-error/15"
      >
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar responsável por nome, email ou CPF"
            ariaLabel="Buscar responsável"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <ButtonLink
            className="sm:ml-auto"
            to="/parents/new"
            variant="success"
          >
            Novo responsável
          </ButtonLink>
        </div>
      </PageHeader>

      {isLoading && <PageLoading message="Carregando responsáveis..." />}

      {error && <ErrorCard description={getFriendlyErrorMessage(error)} onAction={refetch} />}

      <div className="app-table-wrap">
        <div className="overflow-x-auto">
          <table className="table table-zebra w-full">
            <thead className="bg-base-200/90">
              <tr>
                <th className="app-th">Nome</th>
                <th className="app-th hidden lg:table-cell">Email</th>
                <th className="app-th hidden lg:table-cell">CPF</th>
                <th className="app-th">Ações</th>
                <th className="app-th-center">Status</th>
              </tr>
            </thead>
            <tbody>
              {parentsPage?.content?.map((parent) => (
                <tr
                  className="transition-colors hover:bg-base-200/70"
                  key={parent.id}
                >
                  <td>{parent.name}</td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">
                    {parent.email}
                  </td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">
                    {parent.cpf}
                  </td>
                  <td>
                    <ButtonLink
                      size="sm"
                      to={`/parents/${parent.id}`}
                      variant="outline"
                    >
                      Detalhes
                    </ButtonLink>
                  </td>
                  <td className="text-center">
                    <span
                      className={`badge ${parent.archivedAt ? "badge-warning" : "badge-success"}`}
                    >
                      {parent.archivedAt ? "Arquivado" : "Ativo"}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <Pagination
        currentPage={currentPage}
        totalElements={parentsPage?.page?.totalElements ?? 0}
        totalPages={parentsPage?.page?.totalPages ?? 0}
        currentElementsCount={parentsPage?.page?.totalElements ?? 0}
        itemName="responsáveis"
        onPageChange={setCurrentPage}
      />

      {parentsPage?.content?.length === 0 && debouncedSearchTerm === "" && (
        <EmptyCard
          title="Nenhum responsável encontrado"
          description=""
          action={
            <ButtonLink to="/parents/new" variant="secondary">
              Novo aluno
            </ButtonLink>
          }
        />
      )}
    </div>
  );
}
