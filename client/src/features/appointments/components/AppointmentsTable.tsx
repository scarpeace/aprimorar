import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetAtendimentos, type AtendimentoResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/features/appointments/lib/eventContentLables.ts";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";
import { AppointmentMobileCard } from "./AppointmentMobileCard";
import { useAppointmentsFilters } from "../hooks/use-appointments-filters";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter";


export function AppointmentsTable() {
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
  } = useAppointmentsFilters();

  const { startDate, endDate } = usePageDateFilter()

  const eventsQuery = useGetAtendimentos({
    page,
    size: 20,
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
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
          <ListSearchInput
            className="grow"
            placeholder="Buscar por aluno, colaborador, conteudo ou descricao"
            ariaLabel="Buscar atendimento"
            value={search}
            onChange={handleSearchChange}
          />
          <div className="flex w-full flex-row items-start justify-between gap-3 xl:w-auto xl:justify-end">
            <ToggleSwitch
              label="Cobranca pendente"
              toggled={hideCharged}
              setToggle={handleHideChargedToggle}
              tip="Mostrar apenas eventos onde o aluno ainda nao foi cobrado"
              className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
            />
            <ToggleSwitch
              label="Pagamento pendente"
              toggled={hidePaid}
              setToggle={handleHidePaidToggle}
              tip="Mostrar apenas eventos onde o colaborador ainda nao foi pago"
              className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
            />
          </div>
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
              {eventsQuery.data.content.map((event: AtendimentoResponseDTO) => (
                <tr
                  key={event.id}
                  className="group cursor-pointer transition-colors hover:bg-base-200/50"
                  onClick={() => navigate(`/appointments/${event.id}`)}
                >
                  <td>
                    <div className="font-semibold text-base-content">{event.studentName}</div>
                  </td>
                  <td>{event.employeeName}</td>
                  <td>{formatDateShortYear(event.startDate)}</td>
                  <td className="text-center text-sm font-medium">
                    {formatTime(event.startDate)} - {formatTime(event.endDate)}
                  </td>
                  <td className="text-center">
                    <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                      {EventContentLabels[event.content] || event.content}
                    </span>
                  </td>
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                      <span
                        className={`inline-block h-2.5 w-2.5 rounded-full ${event.studentChargeDate ? "bg-success" : "bg-warning"}`}
                        title={event.studentChargeDate ? "Cobrado" : "Pendente"}
                      />
                      <span>{brl.format(event.price)}</span>
                    </div>
                  </td>
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                      <span
                        className={`inline-block h-2.5 w-2.5 rounded-full ${event.employeePaymentDate ? "bg-success" : "bg-warning"}`}
                        title={event.employeePaymentDate ? "Pago" : "Pendente"}
                      />
                      <span>{brl.format(event.payment)}</span>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="flex flex-col gap-4 md:hidden">
        {eventsQuery.data.content.map((event: AtendimentoResponseDTO, index: number) => (
          <AppointmentMobileCard
            key={event.id}
            event={event}
            index={index}
            isPending={false}
            onToggleCharge={() => navigate(`/appointments/${event.id}`)}
            onTogglePayment={() => navigate(`/appointments/${event.id}`)}
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
