import type { AtendimentoResponse } from "@/kubb";
import { brl } from "@/utils/formatter.ts";
import { formatDateShortYear, formatTime } from "@/utils/date-utils.ts";
import {
  Calendar,
  CircleDollarSign,
  Clock3,
  GraduationCap,
  UserRoundCog,
} from "lucide-react";
import { useNavigate } from "react-router-dom";
import {MiniCard} from "@/components/MiniCard.tsx";

type AtendimentoInfoSectionProps = {
  atendimento: AtendimentoResponse;
};

export function AtendimentoInfoSection({
  atendimento,
}: Readonly<AtendimentoInfoSectionProps>) {
  const navigate = useNavigate();

  return (
    <div className="grid gap-3 sm:grid-cols-2">
      <div className="tooltip" data-tip="Acessar Detalhes do Aluno">
        <MiniCard className="hover:bg-base-200 hover:cursor-pointer" label="Aluno" icon={GraduationCap} onClick={() => { navigate(`/alunos/${atendimento.alunoId}`); }}>
          <div className="text-2xl font-semibold text-base-content">
            {atendimento.nomeAluno}
          </div>
        </MiniCard>
      </div>
      <div className="tooltip" data-tip="Acessar Detalhes do Colaborador">
        <MiniCard className="hover:bg-base-200 hover:cursor-pointer" label="Colaborador" icon={UserRoundCog} onClick={() => { navigate(`/colaboradores/${atendimento.colaboradorId}`); }}>
          <div className="text-2xl font-semibold text-base-content">
            {atendimento.nomeColaborador}
          </div>
        </MiniCard>
      </div>

      <div className="flex items-center gap-6">
        <MiniCard label="Data" icon={Calendar}>
          <div className="text-base font-semibold text-base-content">
            {formatDateShortYear(atendimento.inicio)}
          </div>
        </MiniCard>
        <MiniCard label="Horário" icon={Clock3}>
          <div className="text-base font-semibold text-base-content">
            {formatTime(atendimento.inicio)} - {formatTime(atendimento.fim)}
          </div>
        </MiniCard>
        <MiniCard label="Valor do atendimento">
          <div className="flex items-center gap-3">
            <div className="font-mono text-xl font-bold text-base-content">
              {brl.format(atendimento.valor)}
            </div>
          </div>
        </MiniCard>
      </div>

      <div className="flex items-center gap-3">
        <MiniCard label="Repasse do colaborador">
          <div className="flex items-center gap-3">
            <div className="font-mono text-xl font-bold text-base-content">
              {brl.format(atendimento.repasse)}
            </div>
          </div>
        </MiniCard>
        <MiniCard label="Lucro" icon={CircleDollarSign}>
          <div className="text-xl font-semibold text-success">
            {brl.format(atendimento.lucro)}
          </div>
        </MiniCard>
      </div>

      <MiniCard label="Observações" className="lg:col-span-2 min-h-25">
        <div className="text-sm text-base-content/70">
          {atendimento.descricao || "Nenhuma observação."}
        </div>
      </MiniCard>
    </div>
  );
}
