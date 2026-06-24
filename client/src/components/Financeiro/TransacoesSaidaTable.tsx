import { Button } from "@/components/Ui/Button.tsx";
import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { LoadingSpinner } from "@/components/Ui/LoadingSpinner.tsx";
import { Pagination } from "@/components/Ui/Pagination.tsx";
import {
  transacaoResponseDTOStatusEnum,
  transacaoResponseDTOTipoEnum,
  useGetTransacoes
} from "@/kubb";
import { useDebounce } from "@/hooks/useDebounce.ts";
import { brl } from "@/utils/formatter.ts";
import { UserPlus } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { formatDateShortYear } from "@/utils/date-utils";
import { EmptyCard } from "../Ui/EmptyCard";
import { TextSearchInput } from "../Ui/TextSearchInput";
import { ToggleSwitch } from "../Ui/ToggleSwitch";

export function TransacoesSaidaTable({ openForm }: { openForm: () => void }) {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [filterStatus, setFilterStatus] = useState<keyof typeof transacaoResponseDTOStatusEnum | null>(transacaoResponseDTOStatusEnum.PENDENTE);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const transacoesQuery = useGetTransacoes({
    page: currentPage,
    sort: ["createdAt,asc"],
    tipo: transacaoResponseDTOTipoEnum.SAIDA,
    status: filterStatus !== null ? filterStatus : undefined,
    busca: debouncedSearchTerm,
  });

  const transacoes = transacoesQuery.data?.content ?? [];
  const hasSaidas = transacoes.length > 0;
  const page = transacoesQuery.data?.page;
  const totalElements = page?.totalElements ?? 0;
  const totalPages = page?.totalPages ?? 0;
  const pageSize = page?.size ?? transacoes.length;

  const handleTogglePendentes = () => {
    if (filterStatus === transacaoResponseDTOStatusEnum.PENDENTE) {
      setFilterStatus(null);
    } else {
      setFilterStatus(transacaoResponseDTOStatusEnum.PENDENTE);
    }
    setCurrentPage(0);
  };

  const handleTogglePagos = () => {
    if (filterStatus === transacaoResponseDTOStatusEnum.PAGO) {
      setFilterStatus(null);
    } else {
      setFilterStatus(transacaoResponseDTOStatusEnum.PAGO);
    }
    setCurrentPage(0);
  };

  const handleToggleCancelados = () => {
    if (filterStatus === transacaoResponseDTOStatusEnum.CANCELADO) {
      setFilterStatus(null);
    } else {
      setFilterStatus(transacaoResponseDTOStatusEnum.CANCELADO);
    }
    setCurrentPage(0);
  };

  const handleToggleAtrasados = () => {
    if (filterStatus === transacaoResponseDTOStatusEnum.ATRASADO) {
      setFilterStatus(null);
    } else {
      setFilterStatus(transacaoResponseDTOStatusEnum.ATRASADO);
    }
    setCurrentPage(0);
  };

  return (
    <main>
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
          <div className="flex justify-between items-center">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Saídas</h3>
            </div>
          </div>

          <TextSearchInput className="grow" placeholder="Nome, email ou CPF" onChange={setSearchTerm}/>

          <ToggleSwitch
            label={"Pendentes"}
            checked={filterStatus != null && filterStatus === transacaoResponseDTOStatusEnum.PENDENTE}
            setToggle={handleTogglePendentes} />

          <ToggleSwitch
            label={"Pagos"}
            checked={filterStatus === transacaoResponseDTOStatusEnum.PAGO}
            setToggle={handleTogglePagos} />

          <ToggleSwitch
            label={"Atrasados"}
            checked={filterStatus === transacaoResponseDTOStatusEnum.ATRASADO}
            setToggle={handleToggleAtrasados} />

          <ToggleSwitch
            label={"Cancelados"}
            checked={filterStatus === transacaoResponseDTOStatusEnum.CANCELADO}
            setToggle={handleToggleCancelados} />

          <Button onClick={() => openForm()} variant="success"><UserPlus size={21} /></Button>
        </div>
      </section>

      {transacoesQuery.isError && (
        <ErrorCard title="Não foi possível carregar a listagem de entradas" error={transacoesQuery.error}/>
      )}

      {transacoesQuery.isLoading && (
        <LoadingSpinner text="Carregando entradas..." />
      )}

      {(!transacoesQuery.isLoading && !transacoesQuery.isError && !hasSaidas) && (
        <EmptyCard title="Nenhuma entrada encontrada" description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados." />
      )}

      {hasSaidas && (
        <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
          <table className="table table-zebra bg-base-100 animate-[fade-up_280ms_ease-out_both]">
            <thead className="bg-base-200/80">
              <tr>
                <th className="text-left font-semibold text-base-content/80">
                  Criada em:
                </th>
                <th className="text-left font-semibold text-base-content/80">
                  Pagante:
                </th>
                <th className="text-left font-semibold text-base-content/80">
                  Recebedor:
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
              {transacoes.map((transacao) => {
                return (
                  <tr
                    key={transacao.id}
                    className="transition-colors hover:bg-base-200/70 hover:cursor-pointer"
                    onClick={() => navigate(`/transacoes/${transacao.id}`)}
                  >
                    <td className="font-bold">{formatDateShortYear(transacao.createdAt ? transacao.createdAt : "")}</td>
                    <td>{"Pagador"}</td>
                    <td>{"Recebedor"}</td>
                    <td>{brl.format(transacao.valor ? transacao.valor : 0)}</td>
                    <td>{transacao.categoria}</td>
                    <td>{transacao.status}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
          <Pagination
            totalElements={totalElements}
            totalPages={totalPages}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
            size={pageSize}
          />
        </div>
      )}
    </main>
  );
}
