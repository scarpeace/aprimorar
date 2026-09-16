import Link, { type LinkProps } from "next/link";
import { ArrowLeft } from "lucide-react";
import type { ReactNode } from "react";

type BackButtonProps = LinkProps & {
  children?: ReactNode;
  className?: string;
};

export function BackButton({ children = "Voltar", className = "", ...props }: Readonly<BackButtonProps>) {
  return (
    <Link className={`btn btn-outline btn-sm ${className}`} {...props}>
      <ArrowLeft size={16} />
      {children}
    </Link>
  );
}
