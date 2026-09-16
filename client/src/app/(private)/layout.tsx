import type { ReactNode } from "react";
import { AuthProvider } from "@/auth/components/AuthProvider";
import { Nav } from "@/components/layout/Nav";

export default function PrivateLayout({
  children,
}: Readonly<{ children: ReactNode }>) {
  return (
    <AuthProvider>
      <Nav>
          {children}
      </Nav>
    </AuthProvider>
  );
}
