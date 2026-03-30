import { useNavigate } from "react-router-dom";
import { Button } from "./button";
import { Divide, ServerCrash } from "lucide-react";

type PageErrorProps = {
  message?: string;
  error?: any;
};

export function PageError({
  message = "Ocorreu um erro inesperado.",
  error,
}: PageErrorProps) {
  const navigate = useNavigate();

  return (
    <div className="app-surface-alt">
    <div className="flex justify-center items-center h-screen">
      <div className="card w-150 bg-red-200 card-xl shadow-xl rounded-2xl">
        <div className="card-body flex-col justify-center items-center gap-12">
          <h2 className="card-title">Algo inesperado aconteceu</h2>
          <ServerCrash className="w-25 h-25 text-red-600" />
          <p>{error?.message || message}</p>
          <div className="justify-end card-actions">
            <Button variant="primary" onClick={() => navigate(-1)}>
              Voltar
            </Button>
          </div>
        </div>
      </div>
      </div>
      </div>
  );
}
