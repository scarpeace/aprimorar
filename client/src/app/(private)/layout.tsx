import type { ReactNode } from "react";
import { Nav } from "@/components/layout/Nav";
import { requireSession } from "@/lib/auth/session";

export default async function PrivateLayout({
  children,
}: Readonly<{ children: ReactNode }>) {
  const session = await requireSession();

  return (
    <Nav user={session.user}>
      <div className="container">
        {children}
      </div>
    </Nav>
  );
}
