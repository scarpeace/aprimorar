type ErrorCardProps = {
  description?: string;
  title: string;
  error?: unknown;
};

export function ErrorCard({ description, title, error }: Readonly<ErrorCardProps>) {
  return (
    <div className="card overflow-hidden border border-error/20 bg-base-100 shadow-md">
      <div className="card-body gap-5 p-8">
        <div className="badge badge-error badge-outline w-fit px-3 py-3">Ops, algo deu errado</div>

        <div className="space-y-2">
          <h2 className="text-2xl font-semibold text-error">{title}</h2>
          {description ? <p className="max-w-2xl text-md leading-6 text-base-content/70">{description}</p> : null}
          {error ? (
            <p className="max-w-2xl text-lg leading-6 text-red-400">
              &quot;{error instanceof Error ? error.message : String(error)}&quot;
            </p>
          ) : null}
        </div>
      </div>
    </div>
  );
}
