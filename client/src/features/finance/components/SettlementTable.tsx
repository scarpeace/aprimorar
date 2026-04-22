import { EventResponseDTO } from "@/kubb/types/EventResponseDTO";
import { brl } from "@/lib/utils/formatter";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import { Check, Clock } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { useUpdateIncomeStatus } from "@/kubb/hooks/events/useUpdateIncomeStatus";
import { useUpdateExpenseStatus } from "@/kubb/hooks/events/useUpdateExpenseStatus";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import { getEventsQueryKey } from "@/kubb/hooks/events/useGetEvents";
import { getFinanceSummaryQueryKey } from "@/kubb/hooks/finance/useGetFinanceSummary";

interface SettlementTableProps {
  events: EventResponseDTO[];
}

export function SettlementTable({ events }: SettlementTableProps) {
  const queryClient = useQueryClient();

  const { mutate: updateIncome, isPending: isUpdatingIncome } = useUpdateIncomeStatus({
    mutation: {
      onSuccess: () => {
        toast.success("Recebimento atualizado");
        queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
      },
      onError: () => toast.error("Erro ao atualizar recebimento"),
    },
  });

  const { mutate: updateExpense, isPending: isUpdatingExpense } = useUpdateExpenseStatus({
    mutation: {
      onSuccess: () => {
        toast.success("Pagamento ao colaborador atualizado");
        queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
      },
      onError: () => toast.error("Erro ao atualizar pagamento"),
    },
  });

  const handleToggleIncome = (event: EventResponseDTO) => {
    const newStatus = event.incomeStatus === "PAID" ? "PENDING" : "PAID";
    updateIncome({ eventId: event.eventId, params: { status: newStatus } });
  };

  const handleToggleExpense = (event: EventResponseDTO) => {
    const newStatus = event.expenseStatus === "PAID" ? "PENDING" : "PAID";
    updateExpense({ eventId: event.eventId, params: { status: newStatus } });
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
                <td>{event.content}</td>
                <td className="text-right">
                  <div className="flex flex-col items-end gap-1">
                    <span className="font-bold">{brl.format(event.price)}</span>
                    <Button
                      size="xs"
                      variant={event.incomeStatus === "PAID" ? "success" : "outline"}
                      onClick={() => handleToggleIncome(event)}
                      isPending={isUpdatingIncome}
                    >
                      {event.incomeStatus === "PAID" ? (
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
                      size="xs"
                      variant={event.expenseStatus === "PAID" ? "success" : "outline"}
                      onClick={() => handleToggleExpense(event)}
                      isPending={isUpdatingExpense}
                    >
                      {event.expenseStatus === "PAID" ? (
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
