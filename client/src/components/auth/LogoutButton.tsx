"use client";

import { LogOut } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

export function LogoutButton({ compact = false }: Readonly<{ compact?: boolean }>) {
  const router = useRouter();
  const [isPending, setIsPending] = useState(false);

  async function handleLogout() {
    setIsPending(true);

    try {
      await fetch("/api/auth/logout", {
        method: "POST",
      });
    } finally {
      router.replace("/login");
      router.refresh();
      setIsPending(false);
    }
  }

  return (
    <button
      className={
        compact
          ? "btn btn-error btn-soft btn-square btn-sm"
          : "w-full rounded-lg border border-error/30 bg-error/10 p-3 text-sm font-semibold text-error transition hover:cursor-pointer hover:bg-error/20 disabled:cursor-not-allowed disabled:opacity-70"
      }
      disabled={isPending}
      onClick={handleLogout}
      type="button"
      aria-label="Sair"
      title="Sair"
    >
      {compact ? <LogOut size={16} /> : isPending ? "Saindo..." : "Sair"}
    </button>
  );
}
