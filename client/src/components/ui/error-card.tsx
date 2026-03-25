import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { Alert } from "./alert";

type ErrorCardProps = {
  description: string;
  title?: string;
  actionLabel?: string;
  onAction?: () => unknown | Promise<unknown>;
};

export function ErrorCard({
  description,
  title = "Não foi possível carregar",
}: Readonly<ErrorCardProps>) {
  const navigate = useNavigate();
  return (
    <div className="card lg:card-side bg-base-100 shadow-sm">
      <figure>
        <img
          src="https://img.daisyui.com/images/stock/photo-1494232410401-ad00d5433cfa.webp"
          alt="Album"
        />
      </figure>
      <div className="card-body">
        <h2 className="card-title text-error">{title}</h2>
        <p className="text-error/60">{description}</p>
        <div className="card-actions justify-end">
          <Button onClick={() => navigate("/")} variant="outline">
            Voltar para a Página inicial
          </Button>
        </div>
      </div>
    </div>

    // <div className="app-surface card justif border border-error/40 shadow-sm">
    //   <div className="card-body text-center gap-4">
    //     <div className="flex flex-col text-center">
    //       {/*<h2 className="card-title text-error">{title}</h2>*/}
    //       <h2 className="card-title text-error">Titulo do erro</h2>
    //       {/*<p className="app-text-muted text-sm">{description}</p>*/}
    //       <p className="app-text-muted text-sm">Descrição do erro</p>
    //     </div>
    //     {/*{onAction ? (*/}
    //     <div className="card-actions justify-start">
    //       <Button
    //         onClick={() => navigate("/")}
    //         type="button"
    //         variant="outlineError"
    //         size="sm"
    //       >
    //         {actionLabel}
    //       </Button>
    //     </div>
    //     {/*) : null}*/}
    //   </div>
    // </div>
  );
}
