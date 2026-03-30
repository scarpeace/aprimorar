import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import type { StudentResponseDTO } from "@/kubb";
import {
  formatCpf,
  formatPhone,
  formatDateShortYear,
} from "@/lib/utils/formatter";
import type { ReactNode } from "react";
import { useNavigate } from "react-router-dom";

type StudentsTableContentProps = {
  students: StudentResponseDTO[];
};

type StudentsTableStateProps = {
  students: StudentResponseDTO[];
  isLoading: boolean;
  error: unknown;
  children: ReactNode;
};

export function StudentsTableState({
  isLoading,
  error,
  children,
}: Readonly<StudentsTableStateProps>) {

  if (isLoading) {
  return <LoadingCard title="Carregando listagem de alunos" />
}

if (error) {
  return <ErrorCard title="Não foi possível carregar a listagem de alunos" error={error} />
}

  return <>{children}</>;
}

export function StudentsTableContent({
  students,
}: Readonly<StudentsTableContentProps>) {
  const navigate = useNavigate()

  return (
    <table className="table table-zebra w-full p-3 border rounded-xl">
      <thead className="bg-base-200/90">
        <tr>
          <th className="app-th">Nome</th>
          <th className="app-th">CPF</th>
          <th className="app-th">Idade</th>
          <th className="app-th">Contato</th>
          <th className="app-th">Escola</th>
          <th className="app-th">Matricula</th>
          <th className="app-th">Status</th>
          <th className="app-th">Ações</th>
        </tr>
      </thead>

      <tbody>
        {students.map((student) => (
          <tr
            key={student.id}
            className="transition-colors hover:bg-base-200/70"
          >
            <td className="app-td">{student.name}</td>
            <td className="app-td">{formatCpf(student.cpf)}</td>
            <td className="app-td text-center">{student.age}</td>
            <td className="app-td">{formatPhone(student.contact)}</td>
            <td className="app-td">{student.school}</td>
            <td className="app-td">{formatDateShortYear(student.createdAt)}</td>
            <td className="app-td">
              {student.archivedAt ? "Arquivado" : "Ativo"}
            </td>

            {/*TODO: Isso aqui tá dando um erro no console do navegador */}
            <span className="btn m-2 btn-secondary" onClick={()=> navigate(`/students/${student.id}`)}>
              Detalhes
            </span>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
