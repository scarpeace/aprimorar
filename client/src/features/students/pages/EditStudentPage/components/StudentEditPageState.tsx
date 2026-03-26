import  { ErrorCard } from "@/components/ui/error-card";
import { PageLoading } from "@/components/ui/page-loading";
import type { StudentFormInput } from "@/features/students/schemas/studentFormSchema";
import type { StudentResponseDTO } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import type { ReactNode } from "react";

type StudentEditStateProps = {
  studentId: string;
  student?: StudentResponseDTO | undefined;
  isLoading: boolean;
  error: unknown;
  onRetry: () => void;
  children: ReactNode;
};

export function StudentEditPageState({
  studentId,
  student,
  isLoading,
  error,
  onRetry,
  children,
}: Readonly<StudentEditStateProps>) {
  if (isLoading) {
    return <PageLoading message="Carregando aluno para edição..." />;
  }

  if (!studentId || !student) {
    return <ErrorCard description="Aluno não encontrado." />;
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
