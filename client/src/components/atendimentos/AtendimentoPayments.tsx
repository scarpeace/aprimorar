"use client";

import type { AtendimentoResponse } from "@/lib/api/generated/types/AtendimentoResponse";
import { AtendimentoPaymentBadge } from "@/components/atendimentos/AtendimentoPaymentBadge";
import { DetailField } from "@/components/ui/DetailField";
import { useAtendimentoMutations } from "@/hooks/use-atendimento-mutations";
import { formatDate } from "@/lib/utils/formatter";

type AtendimentoPaymentsProps = {
  atendimento: AtendimentoResponse;
};

export function AtendimentoPayments({ atendimento }: Readonly<AtendimentoPaymentsProps>) {
  const { togglePagamentoAluno, toggleRepasseColaborador } = useAtendimentoMutations();
  const isPending = togglePagamentoAluno.isPending || toggleRepasseColaborador.isPending;
  const studentPaid = !!atendimento.dataPagamentoAluno;
  const collaboratorPaid = !!atendimento.dataRepasseColaborador;

  function handleStudentPaymentChange() {
    togglePagamentoAluno.mutate({ id: atendimento.id });
  }

  function handleCollaboratorPaymentChange() {
    toggleRepasseColaborador.mutate({ id: atendimento.id });
  }

  return (
    <div className="app-shell-card p-6">
      <h3 className="text-lg font-bold text-base-content">Pagamento</h3>

      <div className="mt-5 grid gap-5 md:grid-cols-2">
        <div className="space-y-4">
          <div className="flex justify-between">
            <AtendimentoPaymentBadge label="Aluno" paidAt={atendimento.dataPagamentoAluno} />
            <DetailField label="Data" value={formatDate(atendimento.dataPagamentoAluno)} />
          </div>

          <span className="text-xs text-base-content/70">ID: {atendimento.alunoId}</span>

          <label
            className={`label cursor-pointer justify-start gap-3 rounded-xl border bg-base-100 px-4 py-3 ${
              studentPaid ? "border-success/40" : "border-warning/40"
            }`}
          >
            <span className="label-text text-sm text-base-content/70">Pagamento do aluno</span>
            <input
              type="checkbox"
              className={`checkbox checkbox-sm ${studentPaid ? "checkbox-success" : "checkbox-warning"}`}
              checked={studentPaid}
              disabled={isPending}
              onChange={handleStudentPaymentChange}
            />
          </label>
        </div>

        <div className="space-y-4 md:border-l md:border-base-300 md:pl-6">
          <div className="flex justify-between gap-2">
            <AtendimentoPaymentBadge label="Colab." paidAt={atendimento.dataRepasseColaborador} />
            <DetailField label="Data" value={formatDate(atendimento.dataRepasseColaborador)} />
          </div>

          <span className="text-xs text-base-content/70">ID: {atendimento.colaboradorId}</span>

          <label
            className={`label cursor-pointer justify-start gap-3 rounded-xl border bg-base-100 px-4 py-3 ${
              collaboratorPaid ? "border-success/40" : "border-warning/40"
            }`}
          >
            <span className="label-text text-sm text-base-content/70">Repasse do colaborador</span>
            <input
              type="checkbox"
              className={`checkbox checkbox-sm ${collaboratorPaid ? "checkbox-success" : "checkbox-warning"}`}
              checked={collaboratorPaid}
              disabled={isPending}
              onChange={handleCollaboratorPaymentChange}
            />
          </label>
        </div>
      </div>
    </div>
  );
}
