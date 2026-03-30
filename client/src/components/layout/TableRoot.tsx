import type { ReactNode } from "react";

type TableRootProps = {
  children: ReactNode;
  className?: string;
};

export function TableRoot({ children, className }: Readonly<TableRootProps>) {
  return <div className={`app-table-wrap overflow-x-auto ${className}`}>{children}</div>;
}
