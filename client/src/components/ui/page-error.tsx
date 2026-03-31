import { useNavigate } from "react-router-dom";
import { Button } from "./button";
import { ServerCrash } from "lucide-react";

type PageErrorProps = {
  message?: string;
  error?: unknown;
};

export function PageError({
  message = "Ocorreu um erro inesperado.",
  error,
}: PageErrorProps) {
  const navigate = useNavigate();

  const errorMessage =
    error instanceof Error
      ? error.message
      : typeof error === "object" && error !== null && "message" in error
      ? String(error.message)
      : undefined;

  return (
    <div className="bg-base-200">
    <div className="flex justify-center items-center h-screen">
      <div className="card w-150 bg-red-200 card-xl shadow-xl rounded-2xl">
        <div className="card-body flex-col justify-center items-center gap-12">
          <h2 className="card-title">Algo inesperado aconteceu</h2>
          <ServerCrash className="w-25 h-25 text-red-600" />
          <p>{errorMessage || message}</p>
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
