import type { ReactNode } from "react";

type FinanceKpiCardProps = {
  title: string;
  value: string;
  tone: "success" | "warning" | "primary" | "secondary";
  icon: ReactNode;
};

export function FinanceKpiCard({
  title,
  value,
  tone,
  icon,
}: Readonly<FinanceKpiCardProps>) {
  const toneClasses = {
    success: "text-success",
    warning: "text-warning",
    primary: "text-primary",
    secondary: "text-secondary",
  } as const;

  return (
    <article className={`rounded-xl border border-base-200 bg-base-100 p-4 ${toneClasses[tone]}`}>
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-[11px] font-bold uppercase tracking-[0.2em] text-base-content/45">
            {title}
          </p>
        </div>
        <div className={`flex h-9 w-9 items-center justify-center rounded-xl bg-base-200/60 ${toneClasses[tone]}`}>
          {icon}
        </div>
      </div>
      <p className="font-mono text-2xl font-bold text-base-content">{value}</p>
    </article>
  );
}
