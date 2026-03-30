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
    <header className="mb-3 flex min-w-0 flex-col gap-4">
      <div className="flex min-w-0 items-center gap-3">
          <div className="flex h-26 w-30 items-center justify-center rounded-full bg-success/15">
            <Icon className="h-16 w-16 text-success" />
          </div>
        <div className="w-full flex-col gap-3 flex min-w-0">
          <h1 className="app-text text-3xl font-bold">{title}</h1>
          <p className="text-sm app-text-muted">{description}</p>
          <div className="flex mt-3">{children}</div>
        </div>
      </div>
    </header>
  );
}
