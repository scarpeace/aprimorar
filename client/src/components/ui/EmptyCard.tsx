type EmptyCardProps = {
  title: string;
  description: string;
};

export function EmptyCard({ title, description }: EmptyCardProps) {
  return (
    <div className="card mt-3 m-3 border border-dashed border-base-300 bg-base-100 text-center shadow-sm">
      <div className="card-body gap-4">
        <h2 className="card-title mx-auto">{title}</h2>
        <p className="text-sm text-base-content/70">{description}</p>
      </div>
    </div>
  );
}

