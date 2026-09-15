import type { ReactNode } from "react";
import { AuthProvider } from "@/components/auth-provider";
import { Nav } from "@/components/layout/Nav";

export default function PrivateLayout({
  children,
}: Readonly<{ children: ReactNode }>) {
  return (
    <AuthProvider>
      <Nav>
        <div className="container">
          {children}
        </div>
      </Nav>
    </AuthProvider>
  );
}
