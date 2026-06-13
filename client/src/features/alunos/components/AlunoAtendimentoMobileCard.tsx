import { ButtonLink } from "@/components/ui/button";
import type { AtendimentoResponseDTO } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, Clock, SquareArrowOutUpRight, Tag, User as UserIcon } from "lucide-react";
import { memo } from "react";

type AlunoAtendimentoMobileCardProps = {
  atendimento: AtendimentoResponseDTO;
  colaboradorNome: string;
  index: number;
};

const tipoLabels: Record<AtendimentoResponseDTO["tipo"], string> = {
  AULA: "Aula",
  MENTORIA: "Mentoria",
  TERAPIA: "Terapia",
  ORIENTACAO_VOCACIONAL: "Orientação Vocacional",
  ENEM: "Enem",
  PAS: "PAS",
  OUTRO: "Outro",
};

export const AlunoAtendimentoMobileCard = memo(function AlunoAtendimentoMobileCard({
  atendimento,
  colaboradorNome,
  index,
}: Readonly<AlunoAtendimentoMobileCardProps>) {
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
                 <UserIcon size={12} /> {colaboradorNome}
             </div>
             <div className="flex items-center gap-2 font-bold text-lg">
                <Tag size={16} className="text-primary" />
                {tipoLabels[atendimento.tipo] ?? atendimento.tipo}
             </div>
          </div>
          <div className="text-right">
             <div className="text-xl font-black text-primary">{brl.format(atendimento.valor)}</div>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-2 py-2 border-y border-base-200 my-1">
          <div className="flex items-center gap-2 text-sm text-base-content/70">
            <Calendar size={14} /> {formatDateShortYear(atendimento.inicio)}
          </div>
          <div className="flex items-center gap-2 text-sm text-base-content/70 justify-end">
            <Clock size={14} /> {formatTime(atendimento.inicio)} - {formatTime(atendimento.fim)}
          </div>
        </div>

        <div className="flex gap-2 pt-1">
          <span className={`badge ${atendimento.dataCobrancaAluno ? "badge-success" : "badge-warning"} flex-1 gap-2`}>
            {atendimento.dataCobrancaAluno ? "Cobrado" : "Pendente"}
          </span>
          <ButtonLink to={`/atendimentos/${atendimento.id}`} size="sm" className="gap-2" variant="primary">
            <SquareArrowOutUpRight size={18}/> Detalhes
          </ButtonLink>
        </div>
      </div>
    </div>
  );
});
