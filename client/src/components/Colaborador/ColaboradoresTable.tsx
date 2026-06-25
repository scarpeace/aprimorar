import { Button } from "@/components/Ui/Button.tsx";
import { EmptyCard } from "@/components/Ui/EmptyCard";
import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { LoadingSpinner } from "@/components/Ui/LoadingSpinner.tsx";
import { Pagination } from "@/components/Ui/Pagination.tsx";
import { TextSearchInput } from "@/components/Ui/TextSearchInput";
import { ToggleSwitch } from "@/components/Ui/ToggleSwitch";
import { useGetColaboradores } from "@/kubb";
import { useDebounce } from "@/hooks/useDebounce.ts";
import { formatCpf, formatPhone } from "@/utils/formatter.ts";
import { formatDateShortYear } from "@/utils/date-utils.ts";
import { Plus } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { colaboradorConstants } from "../../utils/constants/colaborador-constants.ts";

export function ColaboradoresTable({ openForm }: { openForm: () => void }) {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const colaboradoresQuery = useGetColaboradores({
    page: currentPage,
    nome: debouncedSearchTerm || undefined,
    ativos: showArchived ? undefined : true,
    sort: ["nome,asc"],
  });

  const colaboradores = colaboradoresQuery.data?.content ?? [];
  const page = colaboradoresQuery.data?.page;
  const totalElements = page?.totalElements ?? 0;
  const totalPages = page?.totalPages ?? 0;
  const pageSize = page?.size ?? colaboradores.length;
  const hasColaboradores = colaboradores.length > 0;

  const handleShowArchived = (value: boolean) => {
    setShowArchived(value);
    setCurrentPage(0);
  };

  return (
    <main>
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <h3 className="text-2xl font-bold text-base-content">Colaboradores cadastrados</h3>
            <p className="text-sm text-base-content/60">
              Clique na linha para abrir os detalhes do cadastro.
            </p>
          </div>

          <TextSearchInput
            className="grow"
            label="Pesquisar"
            placeholder="Buscar colaborador por nome, email ou CPF"
            onChange={(value) => {
              setSearchTerm(value);
              setCurrentPage(0);
            }}
          />

          <ToggleSwitch
            label="Mostrar Arquivados"
            tip="Exibe colaboradores arquivados junto com os ativos"
            checked={showArchived}
            setToggle={handleShowArchived}
            variant="info"
          />

          <Button className="sm:ml-auto" onClick={() => openForm()} variant="success">
            <Plus className="mr-2 h-4 w-4" />
            Novo Colaborador
          </Button>
        </div>
      </section>

      {colaboradoresQuery.isError && (
        <ErrorCard
          title="Não foi possível carregar a listagem de colaboradores"
          error={colaboradoresQuery.error}
        />
      )}

      {colaboradoresQuery.isLoading && (
        <LoadingSpinner text="Carregando colaboradores..." />
      )}

      {!colaboradoresQuery.isLoading && !colaboradoresQuery.isError && !hasColaboradores && (
        <EmptyCard
          title="Nenhum colaborador encontrado"
          description="Ajuste a busca para localizar os cadastros desejados."
        />
      )}

      {hasColaboradores && (
        <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
          <table className="table table-zebra w-full table-fixed bg-base-100 animate-[fade-up_280ms_ease-out_both]">
            <thead className="bg-base-200/80">
              <tr>
                <th className="text-left font-semibold text-base-content/80">Nome</th>
                <th className="text-left font-semibold text-base-content/80">Função</th>
                <th className="text-left font-semibold text-base-content/80">CPF</th>
                <th className="text-left font-semibold text-base-content/80">Contato</th>
                <th className="text-left font-semibold text-base-content/80">Cadastro</th>
                <th className="text-center font-semibold text-base-content/80">Status</th>
              </tr>
            </thead>

            <tbody>
              {colaboradores.map((colaborador) => {
                const isArchived = colaborador.active === false;

                return (
                  <tr
                    key={colaborador.id}
                    className="transition-colors hover:bg-base-300/70 hover:cursor-pointer"
                    onClick={() => navigate(`/colaboradores/${colaborador.id}`)}
                  >
                    <td className="max-w-0 truncate font-bold">{colaborador.nome}</td>
                    <td>
                      <span className="block truncate">
                        {colaboradorConstants[colaborador.funcao] ?? "-"}
                      </span>
                    </td>
                    <td className="whitespace-nowrap">{formatCpf(colaborador.cpf)}</td>
                    <td className="whitespace-nowrap">{formatPhone(colaborador.telefone)}</td>
                    <td className="whitespace-nowrap">{formatDateShortYear(colaborador.createdAt)}</td>
                    <td className="text-center">
                      <span className={`badge ${isArchived ? "badge-ghost" : "badge-success"} badge-sm`}>
                        {isArchived ? "Arquivado" : "Ativo"}
                      </span>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      <Pagination
        size={pageSize}
        totalElements={totalElements}
        totalPages={totalPages}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
    </main>
  );
}
