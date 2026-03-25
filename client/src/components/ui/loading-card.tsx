
type ErrorCardProps = {
  title?: string
}

export function LoadingCard({
  title = "Carregando dados...",
}: Readonly<ErrorCardProps>) {
  return (
    <div className="app-surface card shadow-lg">
      <div className="card-body flex flex-row justify-center gap-4">
        <span className="loading loading-spinner loading-lg text-primary" />
        <h2 className="card-title">{title}</h2>
      </div>
    </div>
  )
}
