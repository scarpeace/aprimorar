import { Button, ButtonLink } from "@/components/ui/button";
import type { AtendimentoResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/features/atendimentos/lib/eventContentLabels";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, CircleDollarSign, Clock, SquareArrowOutUpRight, Tag, User as UserIcon } from "lucide-react";
import { memo } from "react";

interface AlunoAtendimentoMobileCardProps {
  atendimento: AtendimentoResponseDTO;
  index: number;
  isPending: boolean;
  onToggleCharge: (id: string) => void;
}

export const AlunoAtendimentoMobileCard = memo(function AlunoAtendimentoMobileCard({
  atendimento,
  index,
  isPending,
  onToggleCharge
}: AlunoAtendimentoMobileCardProps) {
  const animationDelay = `${(index % 5) * 100}ms`;

  return (
    <div
      className="card bg-base-100 border border-base-300 shadow-sm animate-[fade-up_400ms_ease-out_both]"
      style={{ animationDelay }}
    >
      <div className="card-body p-4 gap-3">
        <div className="flex justify-between items-start">
          <div className="flex flex-col gap-1">
             <div className="flex items-center gap-2 text-xs font-bold text-base-content/50 uppercase tracking-tighter">
                <UserIcon size={12} /> {atendimento.employeeName}
             </div>
             <div className="flex items-center gap-2 font-bold text-lg">
                <Tag size={16} className="text-primary" />
                {EventContentLabels[atendimento.content] || atendimento.content}
             </div>
          </div>
          <div className="text-right">
             <div className="text-xl font-black text-primary">{brl.format(atendimento.price)}</div>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-2 py-2 border-y border-base-200 my-1">
          <div className="flex items-center gap-2 text-sm text-base-content/70">
            <Calendar size={14} /> {formatDateShortYear(atendimento.startDate)}
          </div>
          <div className="flex items-center gap-2 text-sm text-base-content/70 justify-end">
            <Clock size={14} /> {formatTime(atendimento.startDate)} - {formatTime(atendimento.endDate)}
          </div>
        </div>

        <div className="flex gap-2 pt-1">
          <Button
            disabled={isPending}
            className="flex-1 gap-2"
            size="sm"
            variant={atendimento.studentChargeDate != null ? "success" : "warning"}
            onClick={() => onToggleCharge(atendimento.id)}
          >
            <CircleDollarSign size={18}/>
            {atendimento.studentChargeDate != null ? "Cobrado" : "Cobrar"}
          </Button>
          <ButtonLink to={`/appointments/${atendimento.id}`} size="sm" className="gap-2" variant="primary">
            <SquareArrowOutUpRight size={18}/> Detalhes
          </ButtonLink>
        </div>
      </div>
    </div>
  );
});
