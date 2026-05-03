import { ListSearchInput } from "@/components/ui/list-search-input";
import { SectionCard } from "@/components/ui/section-card";
import { useGetEventsByEmployeeId, type EventResponseDTO } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { useState } from "react";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { ErrorCard } from "@/components/ui/error-card";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { Pagination } from "@/components/ui/pagination";
import { useSearchParams } from "react-router-dom";
import { useEventMutations } from "@/features/events/hooks/use-event-mutations";
import { Button, ButtonLink } from "@/components/ui/button";
import {
  BrushCleaning,
  CircleCheck,
  CircleDollarSign,
  SquareArrowOutUpRight,
} from "lucide-react";
import { MonthYearPicker } from "@/components/ui/month-year-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { DateRangeInput } from "@/components/ui/date-range-input";

interface EmployeeEventsTableProps {
  employeeId: string;
}

export function EmployeeEventsTable({ employeeId }: EmployeeEventsTableProps) {
  const [searchParams, setSearchParams] = useSearchParams();
  const [currentPage, setCurrentPage] = useState(0);
  const [searchTerm, setSearchTerm] = useState("");
  const [hidePaid, setHidePaid] = useState(false);
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const startDateStr = searchParams.get("startDate");
  const endDateStr = searchParams.get("endDate");

  const startDate = startDateStr ? new Date(startDateStr) : undefined;
  const endDate = endDateStr ? new Date(endDateStr) : undefined;

  const eventsQuery = useGetEventsByEmployeeId(employeeId, {
    page: currentPage,
    studentName: debouncedSearchTerm,
    sort: ["startDate,desc", "id,asc"],
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    hidePaid,
  });

  const { toggleEmployeePayment } = useEventMutations();

  const handleToggleEmployeePayment = (eventId: string) => {
    toggleEmployeePayment.mutate({ id: eventId });
  };

  const handleStartDateChange = (date: Date) => {
    const newParams = new URLSearchParams(searchParams);
    date.setHours(0, 0, 0, 0);
    newParams.set("startDate", date.toISOString());
    setSearchParams(newParams);
    setCurrentPage(0);
  };

  const handleEndDateChange = (date: Date) => {
    const newParams = new URLSearchParams(searchParams);
    date.setHours(23, 59, 59, 999);
    newParams.set("endDate", date.toISOString());
    setSearchParams(newParams);
    setCurrentPage(0);
  };

  const handleHidePaid = () => {
    const newParams = new URLSearchParams(searchParams);
    newParams.set("paid", "false");
    setSearchParams(newParams);
    setCurrentPage(0);
    setHidePaid(!hidePaid);
  };

  const handleClearFilters = () => {
    setSearchParams(new URLSearchParams());
    setSearchTerm("");
    setHidePaid(false);
    setCurrentPage(0);
  };

  if (eventsQuery.error) {
    return (
      <SectionCard
        title="Atendimentos"
        description="Erro ao carregar atendimentos"
      >
        <ErrorCard
          title="Não foi possível carregar a listagem de Eventos"
          error={eventsQuery.error}
        />
      </SectionCard>
    );
  }

  return (
    <SectionCard
      title="Atendimentos"
      description="Atendimentos vinculados ao colaborador"
    >
      <div className="flex gap-6 mb-3 items-center justify-between w-full">
        <ToggleSwitch
          toggled={hidePaid}
          setToggle={handleHidePaid}
          label={"Ocultar Pagos"}
        />

        <div className="flex items-center gap-2">
          <DateRangeInput
            startDate={startDate}
            endDate={endDate}
            onStartDateChange={handleStartDateChange}
            onEndDateChange={handleEndDateChange}
          />
          {(startDate || endDate) && (
            <div className="tooltip" data-tip="Limpar datas">
              <Button size="sm" variant="outline" onClick={handleClearFilters}>
                <BrushCleaning size={16} />
              </Button>
            </div>
          )}
        </div>

        <ListSearchInput
          placeholder="Buscar por aluno"
          value={searchTerm}
          onChange={(val) => {
            setSearchTerm(val);
            setCurrentPage(0);
          }}
        />
      </div>
      <div className="min-h-30">
        {eventsQuery.isPending && (
          <div className="absolute inset-0 z-10 flex items-center justify-center bg-base-100/50 backdrop-blur-[1px]">
            <LoadingSpinner text="Carregando atendimentos..." />
          </div>
        )}

        <table className="table table-zebra table-auto bg-base-100 overflow-x-auto shadow-sm animate-[fade-up_280ms_ease-out_both]">
          <thead className="bg-base-300 rounded">
            <tr>
              <th className="text-left font-semibold text-base-content/80">
                Aluno
              </th>
              <th className="text-left font-semibold text-base-content/80">
                Data
              </th>
              <th className="text-center font-semibold text-base-content/80">
                Horário
              </th>
              <th className="text-center font-semibold text-base-content/80">
                Conteúdo
              </th>
              <th className="text-right font-semibold text-base-content/80">
                Repasse
              </th>
              <th className="text-center font-semibold text-base-content/80 pr-4">
                Status
              </th>
            </tr>
          </thead>

          <tbody className="whitespace-nowrap">
            {eventsQuery.data?.content?.length === 0 ? (
              <tr>
                <td
                  colSpan={6}
                  className="text-center py-8 text-base-content/50"
                >
                  Nenhum atendimento encontrado no período selecionado.
                </td>
              </tr>
            ) : (
              eventsQuery.data?.content?.map((event: EventResponseDTO) => (
                <tr
                  key={event.eventId}
                  className="transition-colors hover:bg-base-300/70"
                >
                  <td className="font-medium">{event.studentName}</td>
                  <td>{formatDateShortYear(event.startDate)}</td>
                  <td className="text-center text-sm">
                    {formatTime(event.startDate)} - {formatTime(event.endDate)}
                  </td>

                  <td className="text-center">
                    <span className="text-xs uppercase font-bold">
                      {EventContentLabels[event.content] || event.content}
                    </span>
                  </td>

                  <td className="text-right text-sm">
                    {brl.format(event.payment)}
                  </td>

                  <td className="text-center flex justify-center gap-1">
                    <div
                      className="tooltip"
                      data-tip={
                        event.employeePaymentDate != null
                          ? "Cancelar Pagamento"
                          : "Marcar como Pago"
                      }
                    >
                      <Button
                        disabled={toggleEmployeePayment.isPending}
                        className="w-10 p-0"
                        size="sm"
                        variant={
                          event.employeePaymentDate != null
                            ? "success"
                            : "warning"
                        }
                        onClick={() =>
                          handleToggleEmployeePayment(event.eventId)
                        }
                      >
                        <CircleDollarSign size={20} />
                      </Button>
                    </div>
                    <div className="tooltip" data-tip={"Detalhes do Evento"}>
                      <ButtonLink
                        to={`/events/${event.eventId}`}
                        size="sm"
                        className="w-10 p-0"
                        variant="primary"
                      >
                        <SquareArrowOutUpRight size={20} />
                      </ButtonLink>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <Pagination
        paginationData={eventsQuery.data}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
    </SectionCard>
  );
}
