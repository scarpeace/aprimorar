import { type ReactNode } from "react";

type CollapseProps = {
  title: string;
  children: ReactNode;
  className?: string;
};

export function Collapse({ title, children, className }: CollapseProps) {

  return (
    <div className={`collapse collapse-arrow bg-base-100 border-base-300 border rounded-xl ${className}`}>
      <input type="checkbox" />
      <div className="collapse-title text-lg font-bold text-base-content/80">
        {title}
      </div>
      <div className="collapse-content text-sm">
        {children}
      </div>
    </div>
  );
}
