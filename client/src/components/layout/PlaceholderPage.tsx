import Link from "next/link";

interface PlaceholderPageProps {
  title: string;
  description: string;
}

export function PlaceholderPage({ title, description }: Readonly<PlaceholderPageProps>) {
  return (
    <section className="app-shell-card p-6 md:p-8">
      <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Etapa 0</p>
      <h1 className="mt-2 text-3xl font-bold text-base-content">{title}</h1>
      <p className="mt-3 max-w-2xl text-sm text-base-content/65">{description}</p>

      <div className="mt-6 flex flex-wrap gap-3">
        <Link className="btn btn-primary" href="/">
          Ir para dashboard
        </Link>
        <Link className="btn btn-outline" href="/login">
          Ver login placeholder
        </Link>
      </div>
    </section>
  );
}
