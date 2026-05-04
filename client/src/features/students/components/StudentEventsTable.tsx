import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { SectionCard } from "@/components/ui/section-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useEventMutations } from "@/features/events/hooks/use-event-mutations";
import { useGetEventsByStudentId, type EventResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { useDebounce } from "@/lib/shared/use-debounce";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { CircleDollarSign, SquareArrowOutUpRight } from "lucide-react";
import { memo, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { StudentEventMobileCard } from "./StudentEventMobileCard";

interface StudentEventsTableProps {
  studentId: string;
}

export const StudentEventsTable = memo(function StudentEventsTable({ studentId }: StudentEventsTableProps) {
  const [searchParams, setSearchParams] = useSearchParams();
  const [currentPage, setCurrentPage] = useState(0);
  const [searchTerm, setSearchTerm] = useState("");
  const [hideCharged, setHideCharged] = useState(searchParams.get("hideCharged") === "true");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const startDateStr = searchParams.get("startDate");
  const endDateStr = searchParams.get("endDate");

  const eventsQuery = useGetEventsByStudentId(studentId, {
    page: currentPage,
    search: debouncedSearchTerm,
    sort: ["startDate,desc", "id,asc"],
    startDate: startDateStr || undefined,
    endDate: endDateStr || undefined,
    hideCharged
  });

  const { toggleStudentCharge } = useEventMutations();

  const handleToggleStudentCharge = (eventId: string) => {
    toggleStudentCharge.mutate({ id: eventId });
  };

  const handleHideCharged = () => {
    const newParams = new URLSearchParams(searchParams);
    const newValue = !hideCharged;
    if (newValue) {
      newParams.set("hideCharged", "true");
    } else {
      newParams.delete("hideCharged");
    }
    setSearchParams(newParams);
    setCurrentPage(0);
    setHideCharged(newValue);
  };

  if (eventsQuery.error) {
    return (
      <SectionCard
        title="Atendimentos"
        description="Erro ao carregar atendimentos do aluno"
      >
        <ErrorCard
          title="Não foi possível carregar a listagem de Eventos"
          error={eventsQuery.error}
        />
      </SectionCard>
    );
  }

  return (
    <div className="relative min-h-75">
      {eventsQuery.isPending && (
        <div className="flex items-center justify-center bg-base-100/60 backdrop-blur-[2px] rounded-xl">
          <LoadingSpinner text="Carregando atendimentos..." />
        </div>
      )}

      {/* Filter Bar */}
      <div className="flex flex-col sm:flex-row gap-4 mb-3 items-center w-full bg-base-200/50 rounded-xl">
        <div className="w-full sm:max-w-xs">
          <ListSearchInput
            placeholder="Buscar por colaborador..."
            value={searchTerm}
            onChange={(val) => {
              setSearchTerm(val);
              setCurrentPage(0);
            }}
          />
        </div>
         <div className="sm:w-auto">
          <ToggleSwitch toggled={hideCharged} setToggle={handleHideCharged} label={"Ocultar Pagos"} />
        </div>
      </div>

      {/* Desktop View (Table) */}
      <div className="hidden md:block overflow-x-auto rounded-xl border border-base-300 shadow-sm animate-[fade-up_300ms_ease-out_both]">
        <table className="table table-zebra w-full table-auto bg-base-100">
          <thead className="bg-base-300 sticky top-0 z-10">
            <tr>
              <th className="font-bold text-base-content/70">Colaborador</th>
              <th className="font-bold text-base-content/70">Data</th>
              <th className="text-center font-bold text-base-content/70">Horário</th>
              <th className="text-center font-bold text-base-content/70">Conteúdo</th>
              <th className="text-right font-bold text-base-content/70">Cobrança</th>
              <th className="text-center font-bold text-base-content/70 pr-6">Ações</th>
            </tr>
          </thead>

          <tbody className="">
            {eventsQuery.data?.content?.length === 0 ? (
              <tr>
                <td colSpan={6} className="text-center py-12 text-base-content/40 italic">
                  Nenhum atendimento encontrado.
                </td>
              </tr>
            ) : (
              eventsQuery.data?.content?.map((event: EventResponseDTO) => (
                <tr key={event.eventId} className="hover:bg-base-200/50 transition-colors group">
                  <td className="font-semibold">{event.employeeName}</td>
                  <td>{formatDateShortYear(event.startDate)}</td>
                  <td className="text-center text-sm font-medium">
                    {formatTime(event.startDate)} - {formatTime(event.endDate)}
                  </td>
                  <td className="text-center">
                    <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                      {EventContentLabels[event.content] || event.content}
                    </span>
                  </td>
                  <td className="text-right font-mono text-sm">
                    {brl.format(event.price)}
                  </td>
                  <td className="text-center">
                    <div className="flex justify-center gap-1.5 opacity-80 group-hover:opacity-100 transition-opacity">
                      <div className="tooltip" data-tip={event.studentChargeDate != null ? "Cancelar Cobrança" : "Marcar como Cobrado"}>
                        <Button
                          disabled={toggleStudentCharge.isPending}
                          className="w-9 h-9 p-0"
                          size="sm"
                          variant={event.studentChargeDate != null ? "success" : "warning"}
                          onClick={() => handleToggleStudentCharge(event.eventId)}
                        >
                          <CircleDollarSign size={18}/>
                        </Button>
                      </div>
                      <div className="tooltip" data-tip="Detalhes">
                        <ButtonLink to={`/events/${event.eventId}`} size="sm" className="w-9 h-9 p-0" variant="primary">
                          <SquareArrowOutUpRight size={18}/>
                        </ButtonLink>
                      </div>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Mobile View (Cards) */}
      <div className="md:hidden flex flex-col gap-4">
        {eventsQuery.data?.content?.length === 0 ? (
          <div className="text-center py-10 bg-base-200/30 rounded-xl border border-dashed border-base-300 text-base-content/50">
            Nenhum atendimento encontrado.
          </div>
        ) : (
          eventsQuery.data?.content?.map((event: EventResponseDTO, index: number) => (
            <StudentEventMobileCard
              key={event.eventId}
              event={event}
              index={index}
              isPending={toggleStudentCharge.isPending}
              onToggleCharge={handleToggleStudentCharge}
            />
          ))
        )}
      </div>

      <div className="mt-6">
        <Pagination
          paginationData={eventsQuery.data}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
        />
      </div>
    </div>
  );
});
