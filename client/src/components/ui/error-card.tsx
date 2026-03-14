import { Button } from "@/components/ui/button"

type ErrorCardProps = {
  description: string
  title?: string
  actionLabel?: string
  onAction?: () => unknown | Promise<unknown>
}

export function ErrorCard({
  description,
  title = "Não foi possível carregar",
  actionLabel = "Tentar novamente",
  onAction,
}: ErrorCardProps) {
  return (
    <div className="app-surface card border border-error/40 shadow-sm">
      <div className="card-body gap-4">
        <h2 className="card-title text-error">{title}</h2>
        <p className="app-text-muted text-sm">{description}</p>

        {onAction ? (
          <div className="card-actions justify-start">
            <Button
              onClick={() => {
                void onAction()
              }}
              type="button"
              variant="outlineError"
              size="sm"
            >
              {actionLabel}
            </Button>
          </div>
        ) : null}
      </div>
    </div>
  )
}
