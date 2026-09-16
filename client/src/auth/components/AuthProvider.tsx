"use client";

import { useEffect, useState } from "react";
import { refreshOnce } from "@/lib/api/kubb-client";
import { getAccessToken } from "@/auth/token-store";

export function AuthProvider({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    let cancelled = false;

    async function initialize() {
      if (getAccessToken()) {
        setIsReady(true);
        return;
      }

      const token = await refreshOnce();

      if (cancelled) {
        return;
      }

      if (!token) {
        window.location.replace("/login");
        return;
      }

      setIsReady(true);
    }

    void initialize();

    return () => {
      cancelled = true;
    };
  }, []);

  if (!isReady) {
    return <div>Carregando sessão...</div>;
  }

  return children;
}
