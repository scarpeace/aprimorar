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

type AppointmentInfoSectionProps = {
  appointment: AtendimentoResponseDTO;
};

export function AppointmentInfoSection({
  appointment,
}: AppointmentInfoSectionProps) {

  const navigate = useNavigate();
  const studentChargePaid = appointment.studentChargeDate != null;
  const employeePaymentPaid = appointment.employeePaymentDate != null;

  return (
    <>
      <div className="grid gap-3 sm:grid-cols-2">
        <div className="tooltip" data-tip={"Acessar Detalhes do Aluno"}>
          <MiniCard className="hover:bg-base-200 hover:cursor-pointer" label="Aluno" icon={GraduationCap} onClick={()=> {navigate(`/students/${appointment.studentId}`);}}>
            <div className="text-2xl font-semibold text-base-content " >
              {appointment.studentName}
            </div>
          </MiniCard>
        </div>
        <div className="tooltip" data-tip={"Acessar Detalhes do Colaborador"}>
          <MiniCard className="hover:bg-base-200 hover:cursor-pointer" label="Colaborador" icon={UserRoundCog} onClick={()=> {navigate(`/employees/${appointment.employeeId}`);}}>
            <div className="text-2xl font-semibold text-base-content">
              {appointment.employeeName}
            </div>
          </MiniCard>
        </div>

        <div className="flex items-center gap-6">
          <MiniCard label="Data" icon={Calendar}>
            <div className="text-base font-semibold text-base-content">
              {formatDateShortYear(appointment.startDate)}
            </div>
          </MiniCard>
          <MiniCard label="Horario" icon={Clock3}>
            <div className="text-base font-semibold text-base-content">
              {formatTime(appointment.startDate)} -{" "}
              {formatTime(appointment.endDate)}
            </div>
          </MiniCard>
          <MiniCard label="Cobranca do aluno">
            <div className="flex items-center gap-3">
              <span
                className={`h-2.5 w-2.5 rounded-full ${studentChargePaid ? "bg-success" : "bg-warning"}`}
              />
              <div className="font-mono text-xl font-bold text-base-content">
                {brl.format(appointment.price)}
              </div>
            </div>
          </MiniCard>
        </div>

        <div className="flex items-center gap-3">


          <MiniCard label="Repasse do colaborador">
            <div className="flex items-center gap-3">
              <span
                className={`h-2.5 w-2.5 rounded-full ${employeePaymentPaid ? "bg-success" : "bg-warning"}`}
              />
              <div className="font-mono text-xl font-bold text-base-content">
                {brl.format(appointment.payment)}
              </div>
            </div>
          </MiniCard>
          <MiniCard label="Lucro" icon={CircleDollarSign}>
            <div className="text-xl font-semibold text-success">
              {brl.format(appointment.profit)}
            </div>
          </MiniCard>
        </div>

        <MiniCard label="Observacoes" className="lg:col-span-2 min-h-25">
          <div className="text-sm text-base-content/70">
            {appointment.description || "Nenhuma observação."}
          </div>
        </MiniCard>
      </div>
    </>
  );
}
