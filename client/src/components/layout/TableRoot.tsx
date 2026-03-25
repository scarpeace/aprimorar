import type { ReactNode } from "react";

type TableRootProps = {
  children: ReactNode;
};

export function TableRoot({ children }: Readonly<TableRootProps>) {
  return <div className="app-table-wrap overflow-x-auto">{children}</div>;
}
