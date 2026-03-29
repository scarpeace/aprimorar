import type { ReactNode } from "react";
import type { StudentResponseDTO } from "@/kubb";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";

type StudentDetailStateProps = {
  isLoading: boolean;
  onBack: () => void;
  children: ReactNode;
};

export function StudentDetailState({
  isLoading,
  onBack,
  children,
}: Readonly<StudentDetailStateProps>) {
  if (isLoading) {
    return <LoadingCard title="Carregando aluno..." />;

  }

  if (error) {
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
