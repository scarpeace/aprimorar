import { type ReactNode } from "react";

type CollapseProps = {
  title: string;
  children: ReactNode;
};

export function Collapse({ title, children }: CollapseProps) {

  return (
    <div className="collapse collapse-arrow bg-base-100 border-base-300 border">
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
