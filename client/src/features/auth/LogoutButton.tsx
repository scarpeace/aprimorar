"use client";

import { LogOut } from "lucide-react";
import { useRouter } from "next/navigation";
import { useAuthMutations } from "@/hooks/use-auth-mutations";

export function LogoutButton({ compact = false }: Readonly<{ compact?: boolean }>) {
  const router = useRouter();
  const { logout } = useAuthMutations();

  async function handleLogout() {
    try {
      await logout.mutateAsync();
    } finally {
      router.replace("/login");
      router.refresh();
    }
  }

  return (
    <button
      aria-label="Sair"
      className={
        compact
          ? "btn btn-error btn-soft btn-square btn-sm"
          : "w-full rounded-lg border border-error/30 bg-error/10 p-3 text-sm font-semibold text-error transition hover:cursor-pointer hover:bg-error/20 disabled:cursor-not-allowed disabled:opacity-70"
      }
      disabled={logout.isPending}
      onClick={handleLogout}
      title="Sair"
      type="button"
    >
      {compact ? <LogOut size={16} /> : logout.isPending ? "Saindo..." : "Sair"}
    </button>
  );
}
