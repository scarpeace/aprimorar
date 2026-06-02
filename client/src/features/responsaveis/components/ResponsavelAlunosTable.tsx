import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { useGetAlunosByResponsavel } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";

interface ResponsavelAlunosTableProps {
  parentId: string;
}

export function ResponsavelAlunosTable({
  parentId,
}: Readonly<ResponsavelAlunosTableProps>) {
  const navigate = useNavigate();
  const alunosQuery = useGetAlunosByResponsavel(parentId);

  if (alunosQuery.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar os alunos vinculados"
        error={alunosQuery.error}
      />
    );
  }

  if (alunosQuery.isPending) {
    return (
      <div className="flex justify-center py-10">
        <LoadingSpinner text="Carregando alunos vinculados..." />
      </div>
    );
  }

  if (!alunosQuery.data || alunosQuery.data.length === 0) {
    return (
      <EmptyCard
        title="Nenhum aluno vinculado"
        description="Este responsavel ainda nao possui alunos associados."
      />
    );
  }

  const alunos = alunosQuery.data;

  return (
    <>
      <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
        <table className="table table-zebra animate-[fade-up_280ms_ease-out_both]">
          <thead className="bg-base-200/80">
            <tr>
              <th className="text-left font-semibold text-base-content/80">Nome</th>
              <th className="text-left font-semibold text-base-content/80">CPF</th>
              <th className="text-left font-semibold text-base-content/80">Idade</th>
              <th className="text-left font-semibold text-base-content/80">Contato</th>
              <th className="text-left font-semibold text-base-content/80">Escola</th>
              <th className="text-left font-semibold text-base-content/80">Matrícula</th>
              <th className="text-left font-semibold text-base-content/80">Status</th>
            </tr>
          </thead>
        <tbody className="whitespace-nowrap">
            {alunos.map((aluno) => {
              const isArchived = aluno.active === false;

              return (
                <tr
                  key={aluno.id}
                  className="transition-colors hover:cursor-pointer hover:bg-base-200/70"
                  onClick={() => navigate(`/students/${aluno.id}`)}
                >
                  <td>{aluno.name}</td>
                  <td>{formatCpf(aluno.cpf)}</td>
                  <td className="text-center">{aluno.age}</td>
                  <td>{formatPhone(aluno.contact)}</td>
                  <td>{aluno.school}</td>
                  <td>{formatDateShortYear(aluno.createdAt)}</td>
                  <td>
                    <span className={`badge ${isArchived ? "badge-ghost" : "badge-success"} badge-sm`}>
                      {isArchived ? "Inativo" : "Ativo"}
                    </span>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </>
  );
}
