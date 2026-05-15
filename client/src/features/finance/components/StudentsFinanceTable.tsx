import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import type { StudentFinanceSummaryDTO } from "@/kubb";
import { brl } from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";

type StudentsFinanceTableProps = {
  students?: StudentFinanceSummaryDTO[];
  isPending: boolean;
  error: unknown;
};

export function StudentsFinanceTable({
  students,
  isPending,
  error,
}: Readonly<StudentsFinanceTableProps>) {
  const navigate = useNavigate();

  if (error) {
    return (
      <ErrorCard
        title="Nao foi possivel carregar o financeiro dos alunos"
        error={error}
      />
    );
  }

  if (isPending) {
    return <LoadingSpinner text="Carregando financeiro dos alunos..." />;
  }

  if (!students || students.length === 0) {
    return (
      <EmptyCard
        title="Nenhum aluno encontrado"
        description="Nao ha alunos com eventos financeiros no periodo selecionado."
      />
    );
  }

  return (
    <div className="max-h-120 overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra bg-base-100 animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-200/80">
          <tr>
            <th className="text-left font-semibold text-base-content/80">
              Aluno
            </th>
            <th className="text-right font-semibold text-base-content/80">
              Total recebido
            </th>
            <th className="text-right font-semibold text-base-content/80">
              Total pendente
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {students.map((student) => {
            const studentId = student.studentId;
            const totalCharged = student.totalCharged ?? 0;
            const totalPending = student.totalPending ?? 0;

            return (
              <tr
                key={studentId ?? student.studentName}
                className="transition-colors hover:cursor-pointer hover:bg-base-200/70"
                onClick={() => studentId && navigate(`/students/${studentId}`)}
              >
                <td className="font-bold text-base-content">
                  {student.studentName ?? "Aluno sem nome"}
                </td>
                <td className="text-right font-mono font-semibold text-success">
                  {brl.format(totalCharged)}
                </td>
                <td
                  className={`text-right font-mono font-semibold ${
                    totalPending > 0 ? "text-warning" : "text-base-content/45"
                  }`}
                >
                  {brl.format(totalPending)}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
