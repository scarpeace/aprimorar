import { useAuth } from "@/features/auth/lib/use-auth";
import { Navigate } from "react-router-dom";
import type { ReactNode } from "react";

export function AdminOnly({ children }: { children: ReactNode }) {
  const { user } = useAuth();

  if (user?.role !== "ADMIN") {
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
}
