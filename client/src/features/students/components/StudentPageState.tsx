import  { ErrorCard } from "@/components/ui/error-card";
import { PageLoading } from "@/components/ui/page-loading";
import type { StudentResponseDTO } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import type { ReactNode } from "react";

type StudentPageStateProps = {
  isLoading: boolean;
  error: unknown;
  children: ReactNode;
};

export function StudentPageState({
  isLoading,
  children,
  error
}: Readonly<StudentPageStateProps>) {

  // if (isLoading) {
    return <PageLoading message="Carregando aluno..." />;
  // }

  //TODO: adicionar um "Voltar" aqui?
  if (error) {
    return (
      <ErrorCard
        description={getFriendlyErrorMessage(error)}
        actionLabel="Tentar novamente"
      />
    );
  }

  return <>{children}</>;
}
