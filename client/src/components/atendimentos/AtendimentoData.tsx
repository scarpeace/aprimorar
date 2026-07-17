"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import type { AtendimentoResponseStatusEnumKey, AtendimentoResponse } from "@/lib/api/generated/types/AtendimentoResponse";
import { AtendimentoStatusBadge } from "@/components/atendimentos/AtendimentoStatusBadge";
import { AtendimentoTipoBadge } from "@/components/atendimentos/AtendimentoTipoBadge";
import { Button } from "@/components/ui/Button";
import { DetailField } from "@/components/ui/DetailField";
import { useAtendimentoMutations } from "@/hooks/use-atendimento-mutations";
import { atendimentoStatusOptions } from "@/lib/constants/atendimento-constants";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl, formatDate } from "@/lib/utils/formatter";

type AtendimentoDataProps = {
  atendimento: AtendimentoResponse;
  onEdit: () => void;
};

export function AtendimentoData({ atendimento, onEdit }: Readonly<AtendimentoDataProps>) {
  const router = useRouter();
  const { concludeAtendimento, cancelAtendimento, deleteAtendimento } = useAtendimentoMutations();
  const isPending = concludeAtendimento.isPending || cancelAtendimento.isPending || deleteAtendimento.isPending;

  function handleDelete() {
    if (!window.confirm("Deseja mesmo excluir este atendimento? Esta ação não pode ser desfeita.")) {
      return;
    }

    deleteAtendimento.mutate(
      { id: atendimento.id },
      {
        onSuccess: () => {
          router.push("/atendimentos");
        },
      },
    );
  }

  function handleStatusChange(status: AtendimentoResponseStatusEnumKey) {
    if (status === "CONCLUIDO") {
      concludeAtendimento.mutate({ id: atendimento.id });
      return;
    }

    if (status === "CANCELADO") {
      cancelAtendimento.mutate({ id: atendimento.id });
    }
  }

  return (
    <div className="app-shell-card p-6">
      <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
        <div className="space-y-3">
          <h2 className="text-xl font-bold text-base-content">Dados do atendimento</h2>
          <div className="flex flex-wrap gap-2">
            <AtendimentoTipoBadge tipo={atendimento.tipo} />
            <AtendimentoStatusBadge status={atendimento.status} />
          </div>
        </div>

        <div className="flex flex-wrap items-end gap-2">
          <label className="form-control w-full min-w-44 sm:w-44">
            <span className="label-text mb-2 text-sm font-medium text-base-content/70">Status</span>
            <select
              className="select select-bordered select-sm w-full"
              value={atendimento.status}
              disabled={isPending || atendimento.status === "CONCLUIDO"}
              onChange={(event) => handleStatusChange(event.target.value as AtendimentoResponseStatusEnumKey)}
            >
              {atendimentoStatusOptions
                .filter((option) => option.value)
                .map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
            </select>
          </label>

          <Button type="button" size="sm" variant="primary" disabled={isPending} onClick={onEdit}>
            Editar
          </Button>

          <Button type="button" size="sm" variant="error" disabled={isPending} onClick={handleDelete}>
            {deleteAtendimento.isPending ? "Processando..." : "Excluir"}
          </Button>

          <Link className="btn btn-outline btn-sm" href="/atendimentos">
            Voltar
          </Link>
        </div>
      </div>

      <div className="mt-6 grid gap-4 rounded-2xl border border-base-300 bg-base-200/25 p-4 md:grid-cols-2">
        <div className="space-y-1">
          <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Aluno</p>
          <p className="text-2xl font-bold text-base-content">{atendimento.nomeAluno}</p>
        </div>

        <div className="space-y-1">
          <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Colaborador</p>
          <p className="text-xl font-semibold text-base-content">{atendimento.nomeColaborador}</p>
        </div>
      </div>

      <div className="mt-6 grid gap-5 md:grid-cols-2">
        <DetailField label="Data" value={formatDateShortYear(atendimento.dataHoraInicio)} />
        <DetailField
          label="Horário"
          value={`${formatTime(atendimento.dataHoraInicio)} - ${formatTime(atendimento.dataHoraFim)}`}
        />
        <DetailField label="Valor" value={brl.format(atendimento.pagamentoAluno)} />
        <DetailField label="Repasse" value={brl.format(atendimento.repasseColaborador)} />
        <DetailField label="Criado em" value={formatDate(atendimento.createdAt)} />
        <DetailField label="Atualizado em" value={formatDate(atendimento.updatedAt)} />
      </div>
    </div>
  );
}
