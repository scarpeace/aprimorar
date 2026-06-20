import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/error-card.tsx";
import { LoadingSpinner } from "@/components/loading-spinner.tsx";
import { useGetAlunosByResponsavel } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/utils/formatter.ts";
import { useNavigate } from "react-router-dom";

interface ResponsavelAlunosTableProps {
  responsavelId: string;
}

export function ResponsavelAlunosTable({
  responsavelId,
}: Readonly<ResponsavelAlunosTableProps>) {
  const navigate = useNavigate();
  const alunosQuery = useGetAlunosByResponsavel(responsavelId);

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
              const isAlunoArquivado = aluno.active === false;

              return (
                <tr
                  key={aluno.id}
                  className="transition-colors hover:cursor-pointer hover:bg-base-200/70"
                  onClick={() => navigate(`/alunos/${aluno.id}`)}
                >
                  <td>{aluno.nome}</td>
                  <td>{formatCpf(aluno.cpf)}</td>
                  <td className="text-center">{aluno.idade}</td>
                  <td>{formatPhone(aluno.telefone)}</td>
                  <td>{aluno.escola}</td>
                  <td>{formatDateShortYear(aluno.createdAt)}</td>
                  <td>
                    <span className={`badge ${isAlunoArquivado ? "badge-ghost" : "badge-success"} badge-sm`}>
                      {isAlunoArquivado ? "Inativo" : "Ativo"}
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
