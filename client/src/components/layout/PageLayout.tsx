import type { LucideIcon } from "lucide-react";
import type { PropsWithChildren, ReactNode } from "react";

const ICON_BG_VARIANTS = {
  primary: "bg-primary/30 text-primary",
  secondary: "bg-secondary/30 text-secondary",
  accent: "bg-accent/30 text-accent",
  neutral: "bg-neutral/20 text-neutral",
  info: "bg-info/25 text-info",
  success: "bg-success/25 text-success",
  warning: "bg-warning/25 text-warning",
  error: "bg-error/25 text-error",
} as const;

type IconBgVariant = keyof typeof ICON_BG_VARIANTS;

type PageLayoutProps = {
  title: string;
  description: string;
  Icon: LucideIcon;
  children: ReactNode;
  iconBg: IconBgVariant;
};

export function PageLayout({
  title,
  description,
  Icon,
  children,
  iconBg,
}: Readonly<PropsWithChildren<PageLayoutProps>>) {
  const iconBgClass = ICON_BG_VARIANTS[iconBg] ?? ICON_BG_VARIANTS.primary;

  return (
    <>
      <header className="flex justify-between flex-col items-start gap-4 min-w-0 mb-6 sm:flex-row sm:items-center">
        <div className="flex w-full items-center sm:w-auto">
          <div className={`shrink-0 rounded-full p-3 ${iconBgClass}`}>
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
