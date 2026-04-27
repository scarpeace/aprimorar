import { useState } from "react";
import { PageLayout } from "@/components/layout/PageLayout";
import { CheckCircle2 } from "lucide-react";
import { SettlementTable } from "../components/SettlementTable";
import { useGetEmployeeOptions } from "@/kubb/hooks/employee/useGetEmployeeOptions";
import { useGetEvents, useGetStudentsOptions } from "@/kubb";

export function EventSettlementPage() {
  const [studentId, setStudentId] = useState<string>("");
  const [employeeId, setEmployeeId] = useState<string>("");

  const studentOptions = useGetStudentsOptions();
  const employeeOptions = useGetEmployeeOptions();

  const { data: eventsPage, isPending } = useGetEvents({
      studentId: studentId || undefined,
      employeeId: employeeId || undefined,
      status: "COMPLETED",
      size: 100,
    },
  );

  return (
    <PageLayout
      title="Baixa de Atendimentos"
      description="Gerencie os pagamentos de alunos e repasses a colaboradores."
      Icon={CheckCircle2}
      backLink="/finance"
    >
      <div className="flex flex-col gap-6">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 bg-base-200/50 p-4 rounded-xl border border-base-200">
          <fieldset className="fieldset">
            <legend className="fieldset-legend">Filtrar por Aluno</legend>
            <select
              className="select select-bordered w-full"
              value={studentId}
              onChange={(e) => setStudentId(e.target.value)}
            >
              <option value="">Todos os alunos</option>
              {studentOptions.data?.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.name}
                </option>
              ))}
            </select>
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Filtrar por Colaborador</legend>
            <select
              className="select select-bordered w-full"
              value={employeeId}
              onChange={(e) => setEmployeeId(e.target.value)}
            >
              <option value="">Todos os colaboradores</option>
              {employeeOptions.data?.map((e) => (
                <option key={e.id} value={e.id}>
                  {e.name}
                </option>
              ))}
            </select>
          </fieldset>
        </div>

        {isPending ? (
          <div className="flex justify-center py-12">
            <span className="loading loading-spinner loading-lg text-primary"></span>
          </div>
        ) : (
          <SettlementTable events={eventsPage?.content ?? []} />
        )}
      </div>
    </PageLayout>
  );
}
