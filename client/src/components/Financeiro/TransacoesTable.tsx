import { TransacaoFilterSelect } from "@/components/Financeiro/TransacaoFilterSelect";
import { Button } from "@/components/Ui/Button.tsx";
import { EmptyCard } from "@/components/Ui/EmptyCard";
import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { LoadingSpinner } from "@/components/Ui/LoadingSpinner.tsx";
import { Pagination } from "@/components/Ui/Pagination.tsx";
import { TextSearchInput } from "@/components/Ui/TextSearchInput";
import { useDebounce } from "@/hooks/useDebounce.ts";
import {
  type GetTransacoesQueryParamsCategoriaEnumKey,
  type GetTransacoesQueryParamsStatusEnumKey,
  type TransacaoResponseDTOTipoEnumKey,
  getTransacoesQueryParamsCategoriaEnum,
  getTransacoesQueryParamsStatusEnum,
  transacaoResponseDTOTipoEnum,
  useGetTransacoes,
} from "@/kubb";
import { formatDateShortYear } from "@/utils/date-utils";
import { brl } from "@/utils/formatter.ts";
import { BanknoteArrowDown, BanknoteArrowUp } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

type TransacoesTableProps = {
  title: string;
  tipo: TransacaoResponseDTOTipoEnumKey;
  openForm: () => void;
};

const statusOptions = [
  { value: getTransacoesQueryParamsStatusEnum.PENDENTE, label: "Pendentes" },
  { value: getTransacoesQueryParamsStatusEnum.PAGO, label: "Pagos" },
  { value: getTransacoesQueryParamsStatusEnum.ATRASADO, label: "Atrasados" },
  { value: getTransacoesQueryParamsStatusEnum.CANCELADO, label: "Cancelados" },
] as const;

const categoriaOptionsByTipo = {
  [transacaoResponseDTOTipoEnum.ENTRADA]: [
    {
      value: getTransacoesQueryParamsCategoriaEnum.PGTO_ALUNO,
      label: "Pagamento de aluno",
    },
  ],
  [transacaoResponseDTOTipoEnum.SAIDA]: [
    {
      value: getTransacoesQueryParamsCategoriaEnum.PGTO_COLABORADOR,
      label: "Pagamento de colaborador",
    },
    {
      value: getTransacoesQueryParamsCategoriaEnum.INVESTIMENTO,
      label: "Investimento",
    },
    { value: getTransacoesQueryParamsCategoriaEnum.CONTAS, label: "Contas" },
    {
      value: getTransacoesQueryParamsCategoriaEnum.MATERIAL,
      label: "Material",
    },
    { value: getTransacoesQueryParamsCategoriaEnum.DESPENSA, label: "Despesa" },
    {
      value: getTransacoesQueryParamsCategoriaEnum.MANUTENCAO,
      label: "Manutenção",
    },
  ],
} as const;

export function TransacoesTable({
  title,
  tipo,
  openForm,
}: TransacoesTableProps) {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [filterStatus, setFilterStatus] = useState<
    GetTransacoesQueryParamsStatusEnumKey | ""
  >("");
  const [filterCategoria, setFilterCategoria] = useState<
    GetTransacoesQueryParamsCategoriaEnumKey | ""
  >("");

  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const categoriaOptions = categoriaOptionsByTipo[tipo];

  const transacoesQuery = useGetTransacoes({
    page: currentPage,
    sort: ["createdAt,asc"],
    tipo,
    status: filterStatus || undefined,
    categoria: filterCategoria || undefined,
    busca: debouncedSearchTerm,
  });

  const transacoes = transacoesQuery.data?.content ?? [];
  const hasTransacoes = transacoes.length > 0;
  const page = transacoesQuery.data?.page;

  return (
    <main>
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-row gap-3 lg:flex-col lg:justify-between">
          <h3 className="text-2xl font-bold text-base-content">{title}</h3>
          <div className="flex items-end gap-3">
            <TextSearchInput
              label="Pesquisar"
              className="grow"
              placeholder="Insira o nome do pagante ou recebedor"
              onChange={(value) => {
                setSearchTerm(value);
                setCurrentPage(0);
              }}
            />

            <TransacaoFilterSelect
              label="Status"
              value={filterStatus}
              placeholder="Todos os status"
              options={statusOptions}
              onChange={(value) => {
                setFilterStatus(
                  value as GetTransacoesQueryParamsStatusEnumKey | "",
                );
                setCurrentPage(0);
              }}
            />

            <TransacaoFilterSelect
              label="Categoria"
              value={filterCategoria}
              placeholder="Todas as categorias"
              options={categoriaOptions}
              onChange={(value) => {
                setFilterCategoria(
                  value as GetTransacoesQueryParamsCategoriaEnumKey | "",
                );
                setCurrentPage(0);
              }}
            />

            <div
              className="tooltip"
              data-tip={
                tipo === transacaoResponseDTOTipoEnum.ENTRADA
                  ? "Adicionar entrada"
                  : "Adicionar saída"
              }
            >
              <Button
                onClick={openForm}
                variant={
                  tipo === transacaoResponseDTOTipoEnum.ENTRADA
                    ? "success"
                    : "danger"
                }
              >
                {tipo === transacaoResponseDTOTipoEnum.ENTRADA ? (
                  <BanknoteArrowDown size={21} />
                ) : (
                  <BanknoteArrowUp size={21} />
                )}
              </Button>
            </div>
          </div>
        </div>
      </section>

      {transacoesQuery.isError && (
        <ErrorCard
          title={`Não foi possível carregar a listagem de ${title.toLowerCase()}`}
          error={transacoesQuery.error}
        />
      )}

      {transacoesQuery.isLoading && (
        <LoadingSpinner text={`Carregando ${title.toLowerCase()}...`} />
      )}

      {!transacoesQuery.isLoading &&
        !transacoesQuery.isError &&
        !hasTransacoes && (
          <EmptyCard
            title={`Nenhuma ${title.toLowerCase()} encontrada`}
            description="Ajuste a busca ou os filtros para localizar os registros desejados."
          />
        )}

      {hasTransacoes && (
        <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
          <table className="table table-zebra bg-base-100 animate-[fade-up_280ms_ease-out_both]">
            <thead className="bg-base-200/80">
              <tr>
                <th className="text-left font-semibold text-base-content/80">
                  Criada em
                </th>
                <th className="text-left font-semibold text-base-content/80">
                  Pagante
                </th>
                <th className="text-left font-semibold text-base-content/80">
                  Recebedor
                </th>
                <th className="text-left font-semibold text-base-content/80">
                  Valor
                </th>
                <th className="text-left font-semibold text-base-content/80">
                  Categoria
                </th>
                <th className="text-left font-semibold text-base-content/80">
                  Status
                </th>
              </tr>
            </thead>

            <tbody className="whitespace-nowrap">
              {transacoes.map((transacao) => (
                <tr
                  key={transacao.id}
                  className="transition-colors hover:bg-base-200/70 hover:cursor-pointer"
                  onClick={() => navigate(`/transacoes/${transacao.id}`)}
                >
                  <td className="font-bold">
                    {formatDateShortYear(transacao.createdAt ?? "")}
                  </td>
                  <td>{transacao.nomePagador}</td>
                  <td>{transacao.nomeRecebedor}</td>
                  <td>{brl.format(transacao.valor ?? 0)}</td>
                  <td>{transacao.categoria}</td>
                  <td>{transacao.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <Pagination
            totalElements={page?.totalElements ?? 0}
            totalPages={page?.totalPages ?? 0}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
            size={page?.size ?? transacoes.length}
          />
        </div>
      )}
    </main>
  );
}
