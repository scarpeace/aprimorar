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
      <header className="min-w-0 gap-5 mb-6">
        <div className="flex flex-row items-center">
          <Icon className="h-20 w-20 p-3 text-success rounded-full bg-success/15" />
          <div className="flex flex-col gap-1 min-w-0 ml-3 justify-center">
            <h1 className="app-text text-3xl font-bold">{title}</h1>
            <p className="text-sm app-text-muted">{description}</p>
          </div>
          <ButtonLink to={backLink} className="ml-auto" variant="primary-outline" >Voltar</ButtonLink>
        </div>
      </header>
      {children}
    </>
  );
}
