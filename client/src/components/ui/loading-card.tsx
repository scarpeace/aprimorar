
type LoadingCardProps = {
  title: string
}

export function LoadingCard({
  title,
}: Readonly<LoadingCardProps>) {
  return (
    <div className="app-surface card shadow-lg">
      <div className="card-body flex flex-row justify-center gap-4">
        <h2 className="card-title">{title}</h2>
        <span className="loading loading-dots loading-xl"></span>
      </div>
    </div>
  )
}
