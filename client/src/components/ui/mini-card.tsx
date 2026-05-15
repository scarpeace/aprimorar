import type { ReactNode } from "react";
import type { LucideIcon } from "lucide-react";

type MiniCardProps = {
  label: string;
  children: ReactNode;
  icon?: LucideIcon;
  className?: string;
  onClick?: () => void;
};

export function MiniCard({ label, children, icon: Icon, className = "", onClick }: Readonly<MiniCardProps>) {
  return (
    <div className={`rounded-2xl border border-base-300 bg-base-200/40 p-3 shadow-sm ${className}`.trim()} onClick={onClick}>
      <div className="mb-2 flex items-center gap-2 text-sm font-semibold text-base-content/70">
        {Icon ? <Icon className="h-4 w-4 text-primary" /> : null}
        <span>{label}</span>
      </div>
      <div className="text-sm text-base-content">{children}</div>
    </div>
  );
}
