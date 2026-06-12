import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetAtendimentos, type AtendimentoResponseDTO } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";
import { useAtendimentoMutations } from "../hooks/use-atendimento-mutations";
import { useAtendimentosFilters } from "../hooks/use-atendimentos-filters";
import { tipoAtendimentoLabels } from "../lib/tipo-atendimento-labels";
import { AtendimentoMobileCard } from "./AtendimentoMobileCard";

type AtendimentosTableProps = {
  startDate?: Date;
  endDate?: Date;
};

export function AtendimentosTable({ startDate, endDate }: Readonly<AtendimentosTableProps>) {
  const navigate = useNavigate();
  const { alternarCobrancaAluno, alternarPagamentoColaborador } = useAtendimentoMutations();

  const {
    search,
    hideCharged,
    hidePaid,
    page,
    handleSearchChange,
    handleHideChargedToggle,
    handleHidePaidToggle,
    handlePageChange,
  } = useAtendimentosFilters();

  const eventsQuery = useGetAtendimentos({
    page,
    size: 20,
    inicio: startDate?.toISOString(),
    fim: endDate?.toISOString(),
    sort: ["inicio,desc", "id,asc"],
    busca: search || undefined,
    ocultarCobrados: hideCharged || undefined,
    ocultarPagos: hidePaid || undefined,
  });

  const events = eventsQuery.data?.content ?? [];
  const hasEvents = events.length > 0;
  const isTogglePending = alternarCobrancaAluno.isPending || alternarPagamentoColaborador.isPending;

  const handleToggleCharge = (id: string) => {
    alternarCobrancaAluno.mutate({ id });
  };

  const handleTogglePayment = (id: string) => {
    alternarPagamentoColaborador.mutate({ id });
  };

  return (
    <>
      <section className="flex flex-col gap-3 my-3 animate-[fade-up_220ms_ease-out_both] lg:flex-row lg:items-center lg:justify-between">
        <ListSearchInput
          className="grow"
          placeholder="Buscar por aluno, colaborador, tipo ou descrição"
          ariaLabel="Buscar atendimento"
          value={search}
          onChange={handleSearchChange}
        />
        <div className="flex gap-6">
          <ToggleSwitch
            label="Cobrança pendente"
            toggled={hideCharged}
            setToggle={handleHideChargedToggle}
            tip="Mostrar apenas atendimentos onde o aluno ainda não foi cobrado"
            className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
          />
          <ToggleSwitch
            label="Pagamento pendente"
            toggled={hidePaid}
            setToggle={handleHidePaidToggle}
            tip="Mostrar apenas atendimentos onde o colaborador ainda não foi pago"
            className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
          />
        </div>
      </section>

      {eventsQuery.isError && (
        <ErrorCard title="Não foi possível carregar a listagem de atendimentos" error={eventsQuery.error} />
      )}

      {eventsQuery.isLoading && (
        <LoadingSpinner text="Carregando atendimentos..." />
      )}

      {!eventsQuery.isLoading && !eventsQuery.isError && !hasEvents && (
        <EmptyCard
          title="Nenhum atendimento encontrado"
          description="Ajuste a busca ou os filtros para localizar os atendimentos desejados."
        />
      )}

      {hasEvents && (
        <>
          <div className="hidden md:block">
            <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
              <table className="table table-zebra w-full table-auto bg-base-100">
                <thead className="sticky top-0 z-10 bg-base-200/90 backdrop-blur">
                  <tr>
                    <th className="font-bold text-base-content/70">Aluno</th>
                    <th className="font-bold text-base-content/70">Colaborador</th>
                    <th className="font-bold text-base-content/70">Data</th>
                    <th className="text-center font-bold text-base-content/70">Horário</th>
                    <th className="text-center font-bold text-base-content/70">Tipo</th>
                    <th className="text-right font-bold text-base-content/70">Cobrança</th>
                    <th className="text-right font-bold text-base-content/70">Repasse</th>
                  </tr>
                </thead>

                <tbody className="whitespace-nowrap">
                  {events.map((atendimento: AtendimentoResponseDTO) => (
                    <tr
                      key={atendimento.id}
                      className="group cursor-pointer transition-colors hover:bg-base-200/50"
                      onClick={() => navigate(`/atendimentos/${atendimento.id}`)}
                    >
                      <td>
                        <div className="font-semibold text-base-content">{atendimento.alunoNome}</div>
                      </td>
                      <td>{atendimento.colaboradorNome}</td>
                      <td>{formatDateShortYear(atendimento.inicio)}</td>
                      <td className="text-center text-sm font-medium">
                        {formatTime(atendimento.inicio)} - {formatTime(atendimento.fim)}
                      </td>
                      <td className="text-center">
                        <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                          {tipoAtendimentoLabels[atendimento.tipo] ?? atendimento.tipo}
                        </span>
                      </td>
                      <td className="text-right">
                        <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                          <span
                            className={`inline-block h-2.5 w-2.5 rounded-full ${atendimento.dataCobrancaAluno ? "bg-success" : "bg-warning"}`}
                            title={atendimento.dataCobrancaAluno ? "Cobrado" : "Pendente"}
                          />
                          <span>{brl.format(atendimento.valor)}</span>
                        </div>
                      </td>
                      <td className="text-right">
                        <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                          <span
                            className={`inline-block h-2.5 w-2.5 rounded-full ${atendimento.dataPagamentoColaborador ? "bg-success" : "bg-warning"}`}
                            title={atendimento.dataPagamentoColaborador ? "Pago" : "Pendente"}
                          />
                          <span>{brl.format(atendimento.repasse)}</span>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div className="flex flex-col gap-4 md:hidden">
            {events.map((atendimento: AtendimentoResponseDTO, index: number) => (
              <AtendimentoMobileCard
                key={atendimento.id}
                atendimento={atendimento}
                index={index}
                isPending={isTogglePending}
                onToggleCharge={handleToggleCharge}
                onTogglePayment={handleTogglePayment}
              />
            ))}
          </div>
        </>
      )}

      <Pagination
        size={eventsQuery.data?.size ?? 0}
        totalElements={eventsQuery.data?.totalElements ?? 0}
        totalPages={eventsQuery.data?.totalPages ?? 0}
        currentPage={page}
        onPageChange={handlePageChange}
      />
    </>
  );
}
