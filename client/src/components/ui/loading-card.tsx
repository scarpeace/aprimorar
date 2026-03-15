
type ErrorCardProps = {
  description: string
  title?: string
}

export function LoadingCard({
  description,
  title = "Carregando dados",
}: Readonly<ErrorCardProps>) {
  return (
    <div className="app-surface card border border-error/40 shadow-sm">
      <div className="card-body gap-4">
        <h2 className="card-title text-error">{title}</h2>
        <p className="app-text-muted text-sm">{description}</p>
      </div>
    </div>
  )
}
