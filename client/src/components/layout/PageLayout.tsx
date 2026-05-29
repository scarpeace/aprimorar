import type { LucideIcon } from "lucide-react";
import type { PropsWithChildren, ReactNode } from "react";

type PageLayoutProps = {
  title: string;
  description: string;
  Icon: LucideIcon;
  headersKpis?: ReactNode;
  children: ReactNode;
  iconBg: string;
};

export function PageLayout({
  title,
  description,
  Icon,
  children,
  iconBg,
}: Readonly<PropsWithChildren<PageLayoutProps>>) {
  return (
    <>
      <header className="flex justify-between flex-col items-start gap-4 min-w-0 mb-6 sm:flex-row sm:items-center">
        <div className="flex w-full items-center sm:w-auto">
          <div className={`shrink-0 rounded-full bg-${iconBg}/30 p-3`}>
            <Icon size={48} />
          </div>
          <div className="ml-3 flex min-w-0 flex-col justify-center gap-0.5 sm:gap-1">
            <h1 className="app-text truncate text-2xl font-bold sm:text-3xl">
              {title}
            </h1>
            <p className="app-text-muted line-clamp-2 text-xs sm:text-sm">
              {description}
            </p>
          </div>
        </div>
      </header>
      {children}
    </>
  );
}
