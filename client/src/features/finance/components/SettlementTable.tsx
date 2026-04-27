import { brl } from "@/lib/utils/formatter";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import { Check, Clock } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useEventMutations } from "@/features/events/hooks/use-event-mutations";
import type { EventResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/lib/shared/eventContentLables";

interface SettlementTableProps {
  events: EventResponseDTO[];
}

export function SettlementTable({ events }: SettlementTableProps) {
  const { settleStudentCharge, settleEmployeePayment } = useEventMutations({ onSuccessCallback: () => {} });

  const handleToggleIncome = (event: EventResponseDTO) => {
    settleStudentCharge.mutate({ id: event.eventId, params: { charged: !event.studentCharged } });
  };

  const handleToggleExpense = (event: EventResponseDTO) => {
    settleEmployeePayment.mutate({ id: event.eventId, params: { paid: !event.employeePaid } });
  };

  return (
    <div className="overflow-x-auto bg-base-100 rounded-xl shadow-sm border border-base-200">
      <table className="table table-zebra w-full">
        <thead>
          <tr>
            <th>Data</th>
            <th>Aluno</th>
            <th>Colaborador</th>
            <th>Conteúdo</th>
            <th className="text-right">Entrada (Aluno)</th>
            <th className="text-right">Saída (Colab.)</th>
          </tr>
        </thead>
        <tbody>
          {events.length === 0 ? (
            <tr>
              <td colSpan={6} className="text-center py-8 text-base-content/50">
                Nenhum atendimento encontrado para os filtros selecionados.
              </td>
            </tr>
          ) : (
            events.map((event) => (
              <tr key={event.eventId}>
                <td className="whitespace-nowrap">
                  {format(new Date(event.startDate), "dd/MM/yyyy HH:mm", { locale: ptBR })}
                </td>
                <td className="font-medium">{event.studentName}</td>
                <td>{event.employeeName}</td>
                <td>{EventContentLabels[event.content] || event.content}</td>
                <td className="text-right">
                  <div className="flex flex-col items-end gap-1">
                    <span className="font-bold">{brl.format(event.price)}</span>
                    <Button
                      variant={event.studentCharged ? "success" : "outline"}
                      onClick={() => handleToggleIncome(event)}
                      disabled={settleStudentCharge.isPending}
                    >
                      {event.studentCharged ? (
                        <>
                          <Check className="h-3 w-3 mr-1" /> Pago
                        </>
                      ) : (
                        <>
                          <Clock className="h-3 w-3 mr-1" /> Pendente
                        </>
                      )}
                    </Button>
                  </div>
                </td>
                <td className="text-right">
                  <div className="flex flex-col items-end gap-1">
                    <span className="font-bold">{brl.format(event.payment)}</span>
                    <Button
                      variant={event.employeePaid ? "success" : "outline"}
                      onClick={() => handleToggleExpense(event)}
                      disabled={settleEmployeePayment.isPending}
                    >
                      {event.employeePaid ? (
                        <>
                          <Check className="h-3 w-3 mr-1" /> Pago
                        </>
                      ) : (
                        <>
                          <Clock className="h-3 w-3 mr-1" /> Pendente
                        </>
                      )}
                    </Button>
                  </div>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
