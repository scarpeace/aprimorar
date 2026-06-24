import { Navigate } from "react-router-dom";
import type { ReactNode } from "react";
import {useAuth} from "@/auth/authContext.tsx";

export function AdminOnly({ children }: { children: ReactNode }) {
  const { user } = useAuth();

  if (user?.role !== "ADMIN") {
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
}
