import type { ReactNode } from "react";

type TooltipProps = {
  content: string;
  children: ReactNode;
  className?: string;
};

export function Tooltip({ content, children, className = "" }: Readonly<TooltipProps>) {
  return (
    <span className={`tooltip tooltip-top ${className}`.trim()} data-tip={content}>
      {children}
    </span>
  );
}
