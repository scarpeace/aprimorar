import type { ReactNode } from "react";

type FinanceKpiCardProps = {
  title: string;
  subtitle: string;
  value: string;
  tone: "success" | "warning" | "primary" | "secondary";
  icon: ReactNode;
};

export function FinanceKpiCard({
  title,
  subtitle,
  value,
  tone,
  icon,
}: Readonly<FinanceKpiCardProps>) {
  const toneClasses = {
    success:
      "border-success/15 bg-linear-to-br from-success/10 via-base-100 to-base-100 text-success",
    warning:
      "border-warning/15 bg-linear-to-br from-warning/10 via-base-100 to-base-100 text-warning",
    primary:
      "border-primary/15 bg-linear-to-br from-primary/10 via-base-100 to-base-100 text-primary",
    secondary:
      "border-secondary/15 bg-linear-to-br from-secondary/10 via-base-100 to-base-100 text-secondary",
  } as const;

  return (
    <article className={`rounded-2xl border p-4 shadow-sm ${toneClasses[tone]}`}>
      <div className="mb-4 flex items-start justify-between gap-3">
        <div>
          <p className="text-[11px] font-bold uppercase tracking-[0.2em] text-base-content/45">
            {title}
          </p>
          <p className="mt-1 text-sm text-base-content/60">{subtitle}</p>
        </div>
        <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-base-100/85 shadow-sm">
          {icon}
        </div>
      </div>
      <p className="font-mono text-2xl font-bold text-base-content">{value}</p>
    </article>
  );
}
