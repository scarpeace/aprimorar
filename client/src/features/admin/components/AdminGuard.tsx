import { useAuthSession } from "@/features/auth/hooks/useAuthSession";
import { Navigate } from "react-router-dom";
import { ReactNode } from "react";

interface AdminGuardProps {
  children: ReactNode;
  fallback?: ReactNode;
}

export function AdminGuard({ children, fallback = <Navigate to="/" replace /> }: AdminGuardProps) {
  const { currentUser } = useAuthSession();

  if (currentUser?.role !== "ADMIN") {
    return fallback;
  }

  return <>{children}</>;
}
