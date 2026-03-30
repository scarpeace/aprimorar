import { useNavigate } from "react-router-dom";
import { Button } from "./button";

type PageLoadingProps = {
  message?: string;
};

export function PageLoading({ message = "Carregando..." }: PageLoadingProps) {
  const navigate = useNavigate();

  return (
    <div className="app-text-muted flex h-100 justify-center items-center gap-2 text-sm">
      <div className="flex flex-col gap-12">
        <div className="flex gap-2 items-center">
          <span className="text-center">{message}</span>
          <span className="loading loading-dots loading-xs"></span>
        </div>
        <Button onClick={() => navigate(-1)}>Voltar</Button>
      </div>
    </div>
  );
}
