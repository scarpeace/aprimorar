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
            <button
              className="btn btn-outline btn-error btn-sm"
              onClick={() => {
                void onAction()
              }}
              type="button"
            >
              {actionLabel}
            </button>
          </div>
        ) : null}
      </div>
    </div>
  )
}
