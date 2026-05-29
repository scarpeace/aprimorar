import { MiniCard } from "@/components/ui/mini-card";
import type { AtendimentoResponseDTO } from "@/kubb";
import { formatDateShortYear, formatTime, brl } from "@/lib/utils/formatter";
import {
  Calendar,
  Clock3,
  CircleDollarSign,
  GraduationCap,
  UserRoundCog,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

type AtendimentoInfoSectionProps = {
  atendimento: AtendimentoResponseDTO;
};

export function AtendimentoInfoSection({
  atendimento,
}: AtendimentoInfoSectionProps) {

  const navigate = useNavigate();
  const alunoChargePaid = atendimento.studentChargeDate != null;
  const colaboradorPaymentPaid = atendimento.employeePaymentDate != null;

  return (
    <>
      <div className="grid gap-3 sm:grid-cols-2">
        <div className="tooltip" data-tip={"Acessar Detalhes do Aluno"}>
          <MiniCard className="hover:bg-base-200 hover:cursor-pointer" label="Aluno" icon={GraduationCap} onClick={()=> {navigate(`/students/${atendimento.studentId}`);}}>
            <div className="text-2xl font-semibold text-base-content " >
              {atendimento.studentName}
            </div>
          </MiniCard>
        </div>
        <div className="tooltip" data-tip={"Acessar Detalhes do Colaborador"}>
          <MiniCard className="hover:bg-base-200 hover:cursor-pointer" label="Colaborador" icon={UserRoundCog} onClick={()=> {navigate(`/employees/${atendimento.employeeId}`);}}>
            <div className="text-2xl font-semibold text-base-content">
              {atendimento.employeeName}
            </div>
          </MiniCard>
        </div>

        <div className="flex items-center gap-6">
          <MiniCard label="Data" icon={Calendar}>
            <div className="text-base font-semibold text-base-content">
              {formatDateShortYear(atendimento.startDate)}
            </div>
          </MiniCard>
          <MiniCard label="Horario" icon={Clock3}>
            <div className="text-base font-semibold text-base-content">
              {formatTime(atendimento.startDate)} -{" "}
              {formatTime(atendimento.endDate)}
            </div>
          </MiniCard>
          <MiniCard label="Cobranca do aluno">
            <div className="flex items-center gap-3">
              <span
                className={`h-2.5 w-2.5 rounded-full ${alunoChargePaid ? "bg-success" : "bg-warning"}`}
              />
              <div className="font-mono text-xl font-bold text-base-content">
                {brl.format(atendimento.price)}
              </div>
            </div>
          </MiniCard>
        </div>

        <div className="flex items-center gap-3">


          <MiniCard label="Repasse do colaborador">
            <div className="flex items-center gap-3">
              <span
                className={`h-2.5 w-2.5 rounded-full ${colaboradorPaymentPaid ? "bg-success" : "bg-warning"}`}
              />
              <div className="font-mono text-xl font-bold text-base-content">
                {brl.format(atendimento.payment)}
              </div>
            </div>
          </MiniCard>
          <MiniCard label="Lucro" icon={CircleDollarSign}>
            <div className="text-xl font-semibold text-success">
              {brl.format(atendimento.profit)}
            </div>
          </MiniCard>
        </div>

        <MiniCard label="Observacoes" className="lg:col-span-2 min-h-25">
          <div className="text-sm text-base-content/70">
            {atendimento.description || "Nenhuma observação."}
          </div>
        </MiniCard>
      </div>
    </>
  );
}
