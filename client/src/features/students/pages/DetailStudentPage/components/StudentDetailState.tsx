import type { ReactNode } from "react";
import type { StudentResponseDTO } from "@/kubb";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";

type StudentDetailStateProps = {
  student?: StudentResponseDTO;
  isLoading: boolean;
  error: unknown;
  onBack: () => void;
  children: ReactNode;
};

export function StudentDetailState({
  student,
  isLoading,
  error,
  onBack,
  children,
}: Readonly<StudentDetailStateProps>) {
  if (isLoading) {
    return <LoadingCard title="Carregando aluno..." />;

  }

  if (error || !student) {
    return (
      <ErrorCard
        title="Erro ao carregar aluno"
        description={getFriendlyErrorMessage(error)}
        actionLabel="Voltar para listagem de alunos"
        onAction={onBack}
      />
    );
  }

  return <>{children}</>;
}
