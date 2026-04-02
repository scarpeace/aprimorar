import { ErrorCard } from "./error-card";
import { LoadingSpinner } from "./loading-spinner";

type ComponentStateProps = {
  isPending: boolean;
  error: unknown;
};

export function ComponentState({ isPending, error }: ComponentStateProps) {
  if (isPending) {
    return <LoadingSpinner text="Carregando Evento..." />;
  }

  if (error) {
    return <ErrorCard title="Não foi possível carregar o Evento" error={error} />;
  }

  return null;
}
