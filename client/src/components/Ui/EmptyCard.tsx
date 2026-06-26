type EmptyCardProps = {
  title: string
  description: string
}

export function EmptyCard({ title, description }: EmptyCardProps) {
  return (
    <div className="mt-3 text-center m-3 bg-base-100 border-base-300 card border border-dashed shadow-sm">
      <div className="card-body gap-4">
        <h2 className="card-title mx-auto">{title}</h2>
        <p className="text-base-content/70 text-sm">{description}</p>
      </div>
    </div>
  )
}
