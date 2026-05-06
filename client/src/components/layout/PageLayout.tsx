import type { LucideIcon } from "lucide-react";
import type { ReactNode } from "react";

import { ButtonLink } from "../ui/button";

type PageLayoutProps = {
  title: string;
  description: string;
  Icon: LucideIcon;
  backLink: string;
  children: ReactNode;
};

export function PageLayout({
  title,
  description,
  Icon,
  backLink,
  children,
}: Readonly<PageLayoutProps>) {
  return (
    <>
      <header className="flex flex-col sm:flex-row items-start sm:items-center min-w-0 gap-4 mb-6">
        <div className="flex flex-row items-center w-full sm:w-auto">
          <Icon className="h-14 w-14 sm:h-20 sm:w-20 p-2 sm:p-3 text-success rounded-full bg-success/15 shrink-0" />
          <div className="flex flex-col gap-0.5 sm:gap-1 min-w-0 ml-3 justify-center">
            <h1 className="app-text text-2xl sm:text-3xl font-bold truncate">{title}</h1>
            <p className="text-xs sm:text-sm app-text-muted line-clamp-2">{description}</p>
          </div>
        </div>
        <ButtonLink to={backLink} className="sm:ml-auto w-full sm:w-auto" variant="primary-outline" >
          Voltar
        </ButtonLink>
      </header>
      {children}
    </>
  );
}
