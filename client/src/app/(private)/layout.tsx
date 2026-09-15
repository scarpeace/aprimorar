import type { ReactNode } from "react";
import { Nav } from "@/components/layout/Nav";

export default function PrivateLayout({
  children,
}: Readonly<{ children: ReactNode }>) {
  return (
    <Nav>
      <div className="container">
        {children}
      </div>
    </Nav>
  );
}
