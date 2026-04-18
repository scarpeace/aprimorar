import { PageLoading } from "@/components/ui/page-loading";
import type { ReactNode } from "react";

type AuthGateProps = {
  isPending: boolean;
  isAuthenticated: boolean;
  children: ReactNode;
  fallback?: ReactNode;
  loadingMessage?: string;
};

export function AuthGate({
  isPending,
  isAuthenticated,
  children,
  fallback = null,
  loadingMessage = "Verificando sua sessão...",
}: Readonly<AuthGateProps>) {
  if (isPending) {
    return <PageLoading message={loadingMessage} />;
  }

  if (!isAuthenticated) {
    return <>{fallback}</>;
  }

  return <>{children}</>;
}
