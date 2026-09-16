import type { ReactNode } from "react";

type CollapseProps = {
  title: ReactNode;
  children: ReactNode;
  defaultOpen?: boolean;
  className?: string;
};

export function Collapse({ title, children, defaultOpen = false, className = "" }: Readonly<CollapseProps>) {
  return (
    <details className={`collapse collapse-arrow border border-base-300 bg-base-100 ${className}`} open={defaultOpen || undefined}>
      <summary className="collapse-title font-semibold">{title}</summary>
      <div className="collapse-content">{children}</div>
    </details>
  );
}
