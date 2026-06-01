import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetAtendimentos, type AtendimentoResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/features/atendimentos/lib/eventContentLabels";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";
import { AtendimentoMobileCard } from "./AtendimentoMobileCard";
import { useAtendimentosFilters } from "../hooks/use-atendimentos-filters";

type AtendimentosTableProps = {
  startDate?: Date;
  endDate?: Date;
};

export function AtendimentosTable({ startDate, endDate }: AtendimentosTableProps) {
  const navigate = useNavigate();

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
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    sort: ["startDate,desc", "id,asc"],
    search: search || undefined,
    hideCharged: hideCharged || undefined,
    hidePaid: hidePaid || undefined,
  });

  if (eventsQuery.isError) {
    return <ErrorCard title="Não foi possível carregar a listagem de Atendimentos" error={eventsQuery.error}/>;
  }

  if (eventsQuery.isLoading) {
    return <LoadingSpinner text="Carregando Atendimentos..." />;
  }

  if (!eventsQuery.data || !eventsQuery.data.content || eventsQuery.data.totalElements === 0) {
    return (
      <EmptyCard
        title="Nenhum atendimento encontrado"
        description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
      />
    );
  }

  return (
    <>
      <section className="flex flex-row gap-12 my-3 animate-[fade-up_220ms_ease-out_both]">
          <ListSearchInput
            className="grow"
            placeholder="Buscar por aluno, colaborador, conteudo ou descricao"
            ariaLabel="Buscar atendimento"
            value={search}
            onChange={handleSearchChange}
          />
          <div className="flex gap-6">
            <ToggleSwitch
              label="Cobranca pendente"
              toggled={hideCharged}
              setToggle={handleHideChargedToggle}
              tip="Mostrar apenas atendimentoos onde o aluno ainda nao foi cobrado"
              className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
            />
            <ToggleSwitch
              label="Pagamento pendente"
              toggled={hidePaid}
              setToggle={handleHidePaidToggle}
              tip="Mostrar apenas atendimentoos onde o colaborador ainda nao foi pago"
              className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
            />
        </div>
      </section>

      <div className="hidden md:block">
        <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
          <table className="table table-zebra w-full table-auto bg-base-100">
            <thead className="sticky top-0 z-10 bg-base-200/90 backdrop-blur">
              <tr>
                <th className="font-bold text-base-content/70">Aluno</th>
                <th className="font-bold text-base-content/70">Colaborador</th>
                <th className="font-bold text-base-content/70">Data</th>
                <th className="text-center font-bold text-base-content/70">Horario</th>
                <th className="text-center font-bold text-base-content/70">Conteudo</th>
                <th className="text-right font-bold text-base-content/70">Cobranca</th>
                <th className="text-right font-bold text-base-content/70">Repasse</th>
              </tr>
            </thead>

            <tbody className="whitespace-nowrap">
              {eventsQuery.data.content.map((atendimento: AtendimentoResponseDTO) => (
                <tr
                  key={atendimento.id}
                  className="group cursor-pointer transition-colors hover:bg-base-200/50"
                  onClick={() => navigate(`/appointments/${atendimento.id}`)}
                >
                  <td>
                    <div className="font-semibold text-base-content">{atendimento.studentName}</div>
                  </td>
                  <td>{atendimento.employeeName}</td>
                  <td>{formatDateShortYear(atendimento.startDate)}</td>
                  <td className="text-center text-sm font-medium">
                    {formatTime(atendimento.startDate)} - {formatTime(atendimento.endDate)}
                  </td>
                  <td className="text-center">
                    <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                      {EventContentLabels[atendimento.content] || atendimento.content}
                    </span>
                  </td>
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                      <span
                        className={`inline-block h-2.5 w-2.5 rounded-full ${atendimento.studentChargeDate ? "bg-success" : "bg-warning"}`}
                        title={atendimento.studentChargeDate ? "Cobrado" : "Pendente"}
                      />
                      <span>{brl.format(atendimento.price)}</span>
                    </div>
                  </td>
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                      <span
                        className={`inline-block h-2.5 w-2.5 rounded-full ${atendimento.employeePaymentDate ? "bg-success" : "bg-warning"}`}
                        title={atendimento.employeePaymentDate ? "Pago" : "Pendente"}
                      />
                      <span>{brl.format(atendimento.payment)}</span>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="flex flex-col gap-4 md:hidden">
        {eventsQuery.data.content.map((atendimento: AtendimentoResponseDTO, index: number) => (
          <AtendimentoMobileCard
            key={atendimento.id}
            atendimento={atendimento}
            index={index}
            isPending={false}
            onToggleCharge={() => navigate(`/appointments/${atendimento.id}`)}
            onTogglePayment={() => navigate(`/appointments/${atendimento.id}`)}
          />
        ))}
      </div>

      <Pagination
        size={eventsQuery.data.size ?? 0}
        totalElements={eventsQuery.data.totalElements}
        totalPages={eventsQuery.data.totalPages}
        currentPage={page}
        onPageChange={handlePageChange}
      />
    </>
  );
}
