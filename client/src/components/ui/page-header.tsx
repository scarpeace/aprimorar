import type { ReactNode } from "react";
import type { LucideIcon } from "lucide-react";

type PageHeaderProps = {
  title: string;
  description: string;
  Icon: LucideIcon;
  children?: ReactNode;
};

export function PageHeader({
  title,
  description,
  Icon,
  children,
}: Readonly<PageHeaderProps>) {
  return (
    <header className="flex flex-col   min-w-0 gap-5">
      <div className="flex flex-row items-center">
        <Icon className="h-20 w-20 p-3 text-success rounded-full bg-success/15" />
        <div className="flex flex-col gap-1 min-w-0 ml-3">
          <h1 className="app-text text-3xl font-bold">{title}</h1>
          <p className="text-sm app-text-muted">{description}</p>
        </div>
        {children}
      </div>
    </header>
  );
}
