import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOStudentResponseDTO } from "@/kubb";
import {
  brl,
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";

type StudentsTableProps = {
  students?: PageDTOStudentResponseDTO;
  chargedByStudentId?: Map<string, number>;
  pendingByStudentId?: Map<string, number>;
  onPageChange: (page: number) => void;
  currentPage: number;
  isPending: boolean;
  error: unknown;
};

export function StudentsTable({
  students,
  chargedByStudentId,
  pendingByStudentId,
  onPageChange,
  currentPage,
  isPending,
  error,
}: Readonly<StudentsTableProps>) {
  const navigate = useNavigate();

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Alunos" error={error}/>;
  }

  if (isPending) {
    return <LoadingSpinner text="Carregando Alunos..." />;
  }

  if (!students || students.content.length === 0) {
    return (
      <EmptyCard
        title="Nenhum aluno encontrado"
        description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
      />
    );
  }

  return (
    <>
      <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra bg-base-100 animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-200/80">
          <tr>
            <th className="text-left font-semibold text-base-content/80">
              Nome
            </th>
            <th className="text-left font-semibold text-base-content/80">
              CPF
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Idade
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Contato
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Escola
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Matricula
            </th>
            <th className="text-right font-semibold text-base-content/80">
              Pago
            </th>
            <th className="text-right font-semibold text-base-content/80">
              Pendente
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Status
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {students?.content.map((student) => {
            const totalCharged = chargedByStudentId?.get(student.id) ?? 0;
            const totalPending = pendingByStudentId?.get(student.id) ?? 0;

            return (
              <tr
                key={student.id}
                className={`transition-colors hover:bg-base-200/70 hover:cursor-pointer`}
                onClick={() => navigate(`/students/${student.id}`)}
              >
                <td className="font-bold">{student.name}</td>

                <td>{formatCpf(student.cpf)}</td>
                <td className="text-center">{student.age}</td>
                <td>{formatPhone(student.contact)}</td>

                 <td>{student.school}</td>

                 <td>{formatDateShortYear(student.createdAt ?? "")}</td>
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
                 <td>
                   <span className={`badge ${(student.active ?? true) ? "badge-success" : "badge-ghost"} badge-sm`}>
                     {(student.active ?? true) ? "Ativo" : "Arquivado"}
                   </span>
                 </td>
               </tr>
            );
          })}
        </tbody>
      </table>
      </div>
      <Pagination
        paginationData={students}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
