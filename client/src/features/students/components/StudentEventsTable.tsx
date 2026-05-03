import { ListSearchInput } from "@/components/ui/list-search-input";
import { SectionCard } from "@/components/ui/section-card";
import { useGetEventsByStudentId, type EventResponseDTO } from "@/kubb";
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
import { CircleDollarSign, SquareArrowOutUpRight } from "lucide-react";
import { ToggleSwitch } from "@/components/ui/toggle-switch";

interface StudentEventsTableProps {
  studentId: string;
}

export function StudentEventsTable({ studentId }: StudentEventsTableProps) {
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
    <SectionCard
      title="Atendimentos"
      description="Atendimentos vinculados ao aluno"
    >
      <div className="min-h-30">
        {eventsQuery.isPending && (
          <div className="absolute inset-0 z-10 flex items-center justify-center bg-base-100/50 backdrop-blur-[1px]">
            <LoadingSpinner text="Carregando atendimentos..." />
          </div>
        )}

        <div className="flex gap-6 mb-3 items-center justify-between w-full">
          <ToggleSwitch toggled={hideCharged} setToggle={handleHideCharged} label={"Ocultar Pagos"} />

          <ListSearchInput
            placeholder="Buscar por colaborador"
            value={searchTerm}
            onChange={(val) => {
              setSearchTerm(val);
              setCurrentPage(0);
            }}
          />
        </div>

        <table className="table table-zebra table-auto bg-base-100 overflow-x-auto shadow-sm animate-[fade-up_280ms_ease-out_both]">
          <thead className="bg-base-300 rounded">
            <tr>
              <th className="text-left font-semibold text-base-content/80">
                Colaborador
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
                Cobrança
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
                  <td className="font-medium">{event.employeeName}</td>
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
                    {brl.format(event.price)}
                  </td>
                  <td className="text-center flex justify-center gap-1">
                    <div className="tooltip" data-tip={event.studentChargeDate != null ? "Cancelar Cobrança" : "Marcar como Cobrado"}>
                    <Button
                      disabled={toggleStudentCharge.isPending}
                      className="w-10 p-0"
                      size="sm"
                      variant={event.studentChargeDate != null ? "success" : "warning"}
                      onClick={() => handleToggleStudentCharge(event.eventId)}
                    >
                        <CircleDollarSign size={20}/>
                      </Button>
                    </div>
                    <div className="tooltip" data-tip={"Detalhes do Evento"}>

                    <ButtonLink
                      to={`/events/${event.eventId}`}
                      size="sm"
                      className="w-10 p-0"
                      variant="primary"
                    ><SquareArrowOutUpRight size={20}/></ButtonLink>
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
