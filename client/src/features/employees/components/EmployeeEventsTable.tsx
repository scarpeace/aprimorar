import { ListSearchInput } from "@/components/ui/list-search-input";
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
  Calendar,
  CircleDollarSign,
  SquareArrowOutUpRight,
} from "lucide-react";
import { ToggleSwitch } from "@/components/ui/toggle-switch";

interface EmployeeEventsTableProps {
  employeeId: string;
}

export function EmployeeEventsTable({ employeeId }: EmployeeEventsTableProps) {
  const [searchParams, setSearchParams] = useSearchParams();
  const [currentPage, setCurrentPage] = useState(0);
  const [searchTerm, setSearchTerm] = useState("");
  const [hidePaid, setHidePaid] = useState(searchParams.get("hidePaid") === "true");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const startDateStr = searchParams.get("startDate");
  const endDateStr = searchParams.get("endDate");

  const eventsQuery = useGetEventsByEmployeeId(employeeId, {
    page: currentPage,
    studentName: debouncedSearchTerm,
    sort: ["startDate,desc", "id,asc"],
    startDate: startDateStr || undefined,
    endDate: endDateStr || undefined,
    hidePaid,
  });

  const { toggleEmployeePayment } = useEventMutations();

  const handleToggleEmployeePayment = (eventId: string) => {
    toggleEmployeePayment.mutate({ id: eventId });
  };

  const handleHidePaid = () => {
    const newParams = new URLSearchParams(searchParams);
    const newValue = !hidePaid;
    if (newValue) {
      newParams.set("hidePaid", "true");
    } else {
      newParams.delete("hidePaid");
    }
    setSearchParams(newParams);
    setCurrentPage(0);
    setHidePaid(newValue);
  };

  if (eventsQuery.error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Eventos" error={eventsQuery.error} />;
  }

  if (eventsQuery.isLoading) {
    return <LoadingSpinner text="Carregando atendimentos..." />;
  }

  return (
    <div className="p-4 rounded-xl bg-base-100">
      <div className="flex gap-3 mb-6">
        <Calendar className="text-base-content/80" />
        <h3 className="text-xl font-bold text-base-content/80">
          Atendimentos vinculados ao colaborador
        </h3>
      </div>

      <div className="flex gap-6 mb-3 items-center w-full">
        <ListSearchInput placeholder="Buscar por aluno" value={searchTerm} onChange={(val) => {
          setSearchTerm(val);
          setCurrentPage(0);
        }} />
        <ToggleSwitch toggled={hidePaid} setToggle={handleHidePaid} label={"Ocultar Pagos"} />
      </div>

      <div className="min-h-30">
        <table className="table table-zebra table-auto bg-base-100 overflow-x-auto shadow-sm animate-[fade-up_280ms_ease-out_both]">
          <thead className="bg-base-300 rounded">
            <tr>
              <th className="text-left font-semibold text-base-content/80">Aluno</th>
              <th className="text-left font-semibold text-base-content/80">Data</th>
              <th className="text-center font-semibold text-base-content/80">Horário</th>
              <th className="text-center font-semibold text-base-content/80">Conteúdo</th>
              <th className="text-right font-semibold text-base-content/80">Repasse</th>
              <th className="text-center font-semibold text-base-content/80 pr-4">Status</th>
            </tr>
          </thead>

          <tbody className="whitespace-nowrap">
            {eventsQuery.data?.content?.map((event: EventResponseDTO) => (
                <tr key={event.eventId} className="transition-colors hover:bg-base-300/70">
                  <td className="font-medium">{event.studentName}</td>
                  <td>{formatDateShortYear(event.startDate)}</td>
                  <td className="text-center text-sm"> {formatTime(event.startDate)} - {formatTime(event.endDate)}</td>

                  <td className="text-center">
                    <span className="text-xs uppercase font-bold">
                      {EventContentLabels[event.content] || event.content}
                    </span>
                  </td>

                  <td className="text-right text-sm">{brl.format(event.payment)}</td>

                  <td className="text-center flex justify-center gap-1">
                    <div className="tooltip" data-tip={event.employeePaymentDate != null ? "Cancelar Pagamento" : "Marcar como Pago"}>
                      <Button
                        disabled={toggleEmployeePayment.isPending}
                        className="w-10 p-0"
                        size="sm"
                        variant={event.employeePaymentDate != null ? "success" : "warning"}
                        onClick={() => handleToggleEmployeePayment(event.eventId)}
                      >
                        <CircleDollarSign size={20} />
                      </Button>
                    </div>
                    <div className="tooltip" data-tip={"Detalhes do Evento"}>
                      <ButtonLink to={`/events/${event.eventId}`} size="sm" className="w-10 p-0" variant="primary">
                        <SquareArrowOutUpRight size={20} />
                      </ButtonLink>
                    </div>
                  </td>
                </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Pagination
        paginationData={eventsQuery.data}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
      </div>
  );
}
