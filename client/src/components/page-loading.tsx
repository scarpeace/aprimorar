import { useNavigate } from "react-router-dom";
import { Button } from "./button.tsx";

type PageLoadingProps = {
  message?: string;
};

export function PageLoading({ message = "Carregando..." }: PageLoadingProps) {
  const navigate = useNavigate();

  return (
    <div className="flex h-100 items-center justify-center gap-2 text-sm text-base-content/70">
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
