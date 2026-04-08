import { type ReactNode } from "react";

type CollapseProps = {
  title: string;
  children: ReactNode;
  className?: string;
};

export function Collapse({ title, children, className }: CollapseProps) {

  return (
    <div className={`collapse collapse-arrow bg-base-100 border-base-300 border ${className}`}>
      <input type="checkbox" />
      <div className="collapse-title font-semibold">
        {title}
      </div>
      <div className="collapse-content text-sm">
        {children}
      </div>
    </div>
  );
}
