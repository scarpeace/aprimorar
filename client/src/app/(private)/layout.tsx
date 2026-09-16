import type { ReactNode } from "react";
import { AuthProvider } from "@/auth/components/AuthProvider";
import { AppDrawer } from "@/components/layout/AppDrawer";

export default function PrivateLayout({
  children,
}: Readonly<{ children: ReactNode }>) {
  return (
    <AuthProvider>
      <AppDrawer>{children}</AppDrawer>
    </AuthProvider>
  );
}
