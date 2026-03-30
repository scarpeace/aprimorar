import { Button } from "@/components/ui/button";
import { AlertTriangle, CircleArrowLeft, Home, RefreshCw } from "lucide-react";
import { useNavigate } from "react-router-dom";

type ErrorCardProps = {
  description?: string;
  title: string;
  error: any;
};

export function ErrorCard({
  description,
  title = "Não foi possível carregar",
  error,
}: Readonly<ErrorCardProps>) {
  const navigate = useNavigate();

  return (
    <div className="card overflow-hidden border border-error/20 bg-base-100 shadow-md">
      <div className="grid lg:grid-cols-[280px_1fr]">
        <div className="relative flex min-h-64 items-center justify-center bg-linear-to-br from-error/10 via-base-200 to-base-100 p-8">
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_left,rgba(255,80,80,0.18),transparent_45%)]" />
          <div className="relative flex h-28 w-28 items-center justify-center rounded-3xl border border-error/20 bg-base-100 shadow-sm">
            <AlertTriangle className="h-14 w-14 text-error" />
          </div>
        </div>

        <div className="card-body justify-center gap-5 p-8">
          <div className="badge badge-error badge-outline w-fit px-3 py-3">
            Ops, algo deu errado
          </div>

          <div className="space-y-2">
            <h2 className="text-2xl font-semibold text-error">{title}</h2>
            <p className="max-w-2xl text-sm leading-6 text-base-content/70">{description || error?.message}</p>
          </div>

          <div className="card-actions justify-end pt-2">
            <Button onClick={() => navigate(-1)} variant="outlineSecondary">
              <CircleArrowLeft className="h-4 w-4" />
              Voltar
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
