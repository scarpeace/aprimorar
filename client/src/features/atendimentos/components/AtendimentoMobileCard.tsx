import { Button, ButtonLink } from "@/components/ui/button";
import type { AtendimentoResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/features/atendimentos/lib/eventContentLabels";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import {
  BriefcaseBusiness,
  Calendar,
  CircleDollarSign,
  Clock3,
  GraduationCap,
  SquareArrowOutUpRight,
  Tag,
} from "lucide-react";
import { memo } from "react";

type AtendimentoMobileCardProps = {
  atendimento: AtendimentoResponseDTO;
  index: number;
  isPending: boolean;
  onToggleCharge: (id: string) => void;
  onTogglePayment: (id: string) => void;
};

export const AtendimentoMobileCard = memo(function AtendimentoMobileCard({
  atendimento,
  index,
  isPending,
  onToggleCharge,
  onTogglePayment,
}: AtendimentoMobileCardProps) {
  const animationDelay = `${(index % 5) * 90}ms`;

  return (
    <div
      className="card border border-base-300 bg-base-100 shadow-sm animate-[fade-up_320ms_ease-out_both]"
      style={{ animationDelay }}
    >
      <div className="card-body gap-4 p-4">
        <div className="flex items-start justify-between gap-3">
          <div className="space-y-2">
            <div className="flex items-center gap-2 text-xs font-bold uppercase tracking-[0.18em] text-base-content/45">
              <Tag className="h-3.5 w-3.5" />
              {EventContentLabels[atendimento.content] || atendimento.content}
            </div>
            <div className="flex items-center gap-2 text-sm text-base-content/70">
              <GraduationCap className="h-4 w-4 text-primary" />
              <span className="font-semibold text-base-content">{atendimento.studentName}</span>
            </div>
            <div className="flex items-center gap-2 text-sm text-base-content/70">
              <BriefcaseBusiness className="h-4 w-4 text-secondary" />
              <span className="font-medium text-base-content">{atendimento.employeeName}</span>
            </div>
          </div>

          <div className="rounded-2xl border border-primary/15 bg-primary/6 px-3 py-2 text-right shadow-sm">
            <div className="text-[11px] font-semibold uppercase tracking-[0.18em] text-base-content/45">
              Receita
            </div>
            <div className="font-mono text-lg font-bold text-base-content">{brl.format(atendimento.price)}</div>
          </div>
        </div>

        <div className="grid grid-cols-1 gap-2 rounded-2xl border border-base-200 bg-base-200/35 p-3 text-sm sm:grid-cols-2">
          <div className="flex items-center gap-2 text-base-content/70">
            <Calendar className="h-4 w-4" />
            <span>{formatDateShortYear(atendimento.startDate)}</span>
          </div>
          <div className="flex items-center gap-2 text-base-content/70 sm:justify-end">
            <Clock3 className="h-4 w-4" />
            <span>
              {formatTime(atendimento.startDate)} - {formatTime(atendimento.endDate)}
            </span>
          </div>
        </div>

        <div className="grid grid-cols-1 gap-2 sm:grid-cols-2">
          <div className="rounded-2xl border border-base-200 bg-base-100 px-3 py-2">
            <div className="mb-1 flex items-center justify-between gap-2 text-xs font-semibold uppercase tracking-[0.16em] text-base-content/45">
              <span>Cobrança</span>
              <span
                className={`inline-block h-2.5 w-2.5 rounded-full ${atendimento.studentChargeDate ? "bg-success" : "bg-warning"}`}
                title={atendimento.studentChargeDate ? "Cobrado" : "Pendente"}
              />
            </div>
            <div className="font-mono text-sm font-semibold text-base-content">{brl.format(atendimento.price)}</div>
          </div>

          <div className="rounded-2xl border border-base-200 bg-base-100 px-3 py-2">
            <div className="mb-1 flex items-center justify-between gap-2 text-xs font-semibold uppercase tracking-[0.16em] text-base-content/45">
              <span>Repasse</span>
              <span
                className={`inline-block h-2.5 w-2.5 rounded-full ${atendimento.employeePaymentDate ? "bg-success" : "bg-warning"}`}
                title={atendimento.employeePaymentDate ? "Pago" : "Pendente"}
              />
            </div>
            <div className="font-mono text-sm font-semibold text-base-content">{brl.format(atendimento.payment)}</div>
          </div>
        </div>

        <div className="flex flex-wrap gap-2 pt-1">
          <Button
            disabled={isPending}
            className="flex-1 gap-2"
            size="sm"
            variant={atendimento.studentChargeDate ? "success" : "warning"}
            onClick={() => onToggleCharge(atendimento.id)}
          >
            <CircleDollarSign className="h-4 w-4" />
            {atendimento.studentChargeDate ? "Cobrado" : "Cobrar"}
          </Button>
          <Button
            disabled={isPending}
            className="flex-1 gap-2"
            size="sm"
            variant={atendimento.employeePaymentDate ? "success" : "warning"}
            onClick={() => onTogglePayment(atendimento.id)}
          >
            <CircleDollarSign className="h-4 w-4" />
            {atendimento.employeePaymentDate ? "Pago" : "Pagar"}
          </Button>
          <ButtonLink to={`/appointments/${atendimento.id}`} size="sm" className="w-full gap-2 sm:w-auto" variant="primary">
            <SquareArrowOutUpRight className="h-4 w-4" />
            Detalhes
          </ButtonLink>
        </div>
      </div>
    </div>
  );
});
