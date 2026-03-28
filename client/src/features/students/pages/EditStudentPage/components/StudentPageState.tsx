import  { ErrorCard } from "@/components/ui/error-card";
import { PageLoading } from "@/components/ui/page-loading";
import type { StudentResponseDTO } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import type { ReactNode } from "react";

type StudentPageStateProps = {
  student?: StudentResponseDTO | undefined;
  isLoading: boolean;
  error: unknown;
  onRetry?: () => void;
  children: ReactNode;
};

export function StudentPageState({
  student,
  isLoading,
  error,
  onRetry,
  children,
}: Readonly<StudentPageStateProps>) {
  if (isLoading) {
    return <PageLoading message="Carregando aluno..." />;
  }

  if (student && student == undefined) {
    return <ErrorCard description="Aluno não encontrado." />;
  }

  //TODO: adicionar um "Voltar" aqui?
  if (error) {
    return (
      <ErrorCard
        description={getFriendlyErrorMessage(error)}
        actionLabel="Tentar novamente"
        onAction={onRetry}
      />
    );
  }

  return <>{children}</>;
}
