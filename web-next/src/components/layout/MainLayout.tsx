"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import type { ReactNode } from "react";
import { LogoutButton } from "@/components/auth/LogoutButton";
import type { AuthUser } from "@/lib/auth/types";

const navigation = [
  { name: "Dashboard", href: "/" },
  { name: "Alunos", href: "/alunos" },
  { name: "Responsáveis", href: "/responsaveis" },
  { name: "Colaboradores", href: "/colaboradores" },
  { name: "Atendimentos", href: "/atendimentos" },
  { name: "Financeiro", href: "/financeiro" },
  { name: "Admin", href: "/admin" },
] as const;

function isActive(pathname: string, href: string) {
  return pathname === href || pathname.startsWith(`${href}/`);
}

export function MainLayout({ children, user }: Readonly<{ children: ReactNode; user: AuthUser }>) {
  const pathname = usePathname();
  const visibleNavigation = user.role === "ADMIN" ? navigation : navigation.filter((item) => item.href !== "/admin");

  return (
    <div className="flex min-h-screen w-full flex-col md:flex-row">
      <aside className="border-b border-base-300 bg-base-100 shadow-sm md:w-72 md:border-r md:border-b-0">
        <div className="border-b border-base-300 px-5 py-5">
          <h1 className="text-xl font-extrabold tracking-tight text-base-content">Aprimorar</h1>
          <p className="mt-1 text-xs font-semibold uppercase tracking-wider text-base-content/60">
            Shell Next.js
          </p>
        </div>

        <nav className="flex gap-2 overflow-x-auto p-4 md:flex-col md:overflow-x-visible">
          {visibleNavigation.map((item) => (
            <Link
              key={item.href}
              href={item.href}
              className={`rounded-lg border px-3 py-2 text-sm font-semibold transition ${
                isActive(pathname, item.href)
                  ? "border-primary/25 bg-base-200 text-base-content shadow-sm"
                  : "border-transparent text-base-content/70 hover:border-base-300 hover:bg-base-200 hover:text-base-content"
              }`}
            >
              {item.name}
            </Link>
          ))}
        </nav>

        <div className="border-t border-base-300 p-4">
          <div className="mb-3 px-3">
            <p className="truncate text-xs font-semibold text-base-content/60">{user.username}</p>
            <p className="text-xs uppercase tracking-wider text-base-content/40">{user.role}</p>
          </div>

          <LogoutButton />
        </div>
      </aside>

      <main className="min-h-screen min-w-0 flex-1">
        <div className="mx-auto w-full min-w-0 px-4 py-5 md:px-8 md:py-7">{children}</div>
      </main>
    </div>
  );
}
