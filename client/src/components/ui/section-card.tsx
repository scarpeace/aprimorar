import type { PropsWithChildren, ReactNode } from "react";

type SectionCardProps = PropsWithChildren<{
  title: string;
  description: string;
  headerActions?: ReactNode;
}>;

export function SectionCard({
  title,
  description,
  headerActions,
  children,
}: Readonly<SectionCardProps>) {
  return (
    <section className={`card border border-base-300 bg-base-100 shadow-sm`}>
      <div className={`card-body`}>
        <div className="card-actions justify-between">
          <div>
            <h2 className="card-title">{title}</h2>
            <p className="text-sm text-base-content/70">{description}</p>
          </div>
          <div className="flex gap-2">
            {headerActions}
            </div>
        </div>

        {children}
      </div>
    </section>
  );
}
