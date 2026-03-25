import  { ErrorCard } from "@/components/ui/error-card";
import { PageLoading } from "@/components/ui/page-loading";
import type { StudentResponseDTO } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import type { ReactNode } from "react";

type StudentEditStateProps = {
  studentId: string;
  student?: StudentResponseDTO;
  isLoading: boolean;
  error: unknown;
  onRetry: () => void;
  children: ReactNode;
};

export function StudentEditState({
  studentId,
  student,
  isLoading,
  error,
  onRetry,
  children,
}: Readonly<StudentEditStateProps>) {
  if (!studentId) {
    return <ErrorCard description="ID do aluno não informado." />;
  }

  if (isLoading) {
    return <PageLoading message="Carregando aluno para edição..." />;
  }

  if (error || !student) {
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
