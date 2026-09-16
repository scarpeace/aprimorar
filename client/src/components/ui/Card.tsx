import type { ComponentProps, ReactNode } from "react";

type CardProps = ComponentProps<"section">;

type CardSlotProps = {
  children: ReactNode;
  className?: string;
};

export function Card({ children, className = "", ...props }: Readonly<CardProps>) {
  return (
    <section className={`card border border-base-300 bg-base-100 shadow-sm ${className}`} {...props}>
      <div className="card-body">{children}</div>
    </section>
  );
}

export function CardHeader({ children, className = "" }: Readonly<CardSlotProps>) {
  return (
    <header className={`flex flex-col gap-4 md:flex-row md:items-start md:justify-between ${className}`}>
      {children}
    </header>
  );
}

export function CardTitle({ children, className = "" }: Readonly<CardSlotProps>) {
  return <h1 className={`card-title text-2xl font-bold uppercase ${className}`}>{children}</h1>;
}

export function CardActions({ children, className = "" }: Readonly<CardSlotProps>) {
  return <div className={`card-actions ${className}`}>{children}</div>;
}
