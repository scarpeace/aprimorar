import type { LucideIcon } from "lucide-react";
import type { PropsWithChildren, ReactNode } from "react";

type PageLayoutProps = {
  title: string;
  description: string;
  Icon: LucideIcon;
  headersKpis?: ReactNode;
  children: ReactNode;
};

export function PageLayout({
  title,
  description,
  Icon,
  headersKpis,
  children,
}: Readonly<PropsWithChildren<PageLayoutProps>>) {
  return (
    <>
      <header className="mb-6 flex justify-between min-w-0 flex-col items-start gap-4 sm:flex-row sm:items-center">
        <div className="flex w-full items-center sm:w-auto">
          <Icon size={60} className="shrink-0 rounded-full bg-success/15 p-2 text-success" />
          <div className="ml-3 flex min-w-0 flex-col justify-center gap-0.5 sm:gap-1">
            <h1 className="app-text truncate text-2xl font-bold sm:text-3xl">{title}</h1>
            <p className="app-text-muted line-clamp-2 text-xs sm:text-sm">{description}</p>
          </div>
        </div>
        { headersKpis}
        {/*<ButtonLink
          to={backLink}
          className="w-full sm:ml-auto sm:w-auto"
          variant="primary-outline"
        >
          Voltar
        </ButtonLink>*/}
      </header>
      {children}
    </>
  );
}
