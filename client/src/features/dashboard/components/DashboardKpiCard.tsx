import type { ReactNode } from "react";

type DashboardKpiCardProps = {
  label: string;
  value: ReactNode;
};
export function DashboardKpiCard({
  label,
  value,
}: Readonly<DashboardKpiCardProps>) {
  return (
    <div className="card border border-base-300 bg-base-100 shadow-(--app-elevation-soft) transition-[transform,box-shadow] duration-200 hover:-translate-y-[0.5] hover:shadow-(--app-elevation-strong) animate-[fade-up_360ms_ease-out_both]">
      <div className="card-body gap-2">
        <h2 className="app-kpi-label">{label}</h2>
        <div className="app-kpi-value">{value}</div>
      </div>
    </div>
  );
}
