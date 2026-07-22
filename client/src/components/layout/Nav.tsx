"use client";

import Link from "next/link";
import Image from "next/image";
import { usePathname } from "next/navigation";
import type { ReactNode } from "react";
import {
  BriefcaseBusiness,
  CalendarDays,
  GraduationCap,
  LayoutDashboard,
  Menu,
  Receipt,
  ShieldUser,
} from "lucide-react";
import { LogoutButton } from "@/components/auth/LogoutButton";
import type { AuthUser } from "@/lib/auth/types";

const navLinkBase = "rounded-lg border px-3 py-2 text-sm font-semibold transition";
const navLinkActive = "border-primary/25 bg-base-200 text-base-content shadow-sm";
const navLinkInactive = "border-transparent text-base-content/70 hover:border-base-300 hover:bg-base-200 hover:text-base-content";

function isActive(pathname: string, href: string) {
  return pathname === href || pathname.startsWith(`${href}/`);
}

function getNavLinkClass(pathname: string, href: string) {
  return `${navLinkBase} ${isActive(pathname, href) ? navLinkActive : navLinkInactive}`;
}

function NavLabel({ icon: Icon, label }: Readonly<{ icon: typeof LayoutDashboard; label: string }>) {
  return (
    <span className="flex items-center gap-2">
      <Icon size={16} />
      <span>{label}</span>
    </span>
  );
}

function UserSummary({ user }: Readonly<{ user: AuthUser }>) {
  return (
    <div className="px-3 py-2">
      <p className="truncate text-xs font-semibold text-base-content/60">{user.username}</p>
      <p className="text-xs uppercase tracking-wider text-base-content/40">{user.role}</p>
    </div>
  );
}

export function Nav({ children, user }: Readonly<{ children: ReactNode; user: AuthUser }>) {
  const pathname = usePathname();
  const isAdmin = user.role === "ADMIN";
  const canAccessDespesas = isAdmin || user.role === "COLABORADOR";

  return (
    <div className="min-h-screen bg-base-200">
      <header className="border-b border-base-300 bg-base-100 shadow-sm">
        <div className="navbar gap-2 px-4 md:px-8">
          <div className="navbar-start w-auto min-w-0 gap-2">
            <div className="dropdown">
              <button type="button" tabIndex={0} className="btn btn-ghost btn-square lg:hidden" aria-label="Abrir navegação">
                <Menu size={18} />
              </button>

              <ul
                tabIndex={0}
                className="menu menu-sm dropdown-content z-20 mt-3 w-64 rounded-box border border-base-300 bg-base-100 p-2 shadow"
              >
                <li>
                  <Link href="/" className={getNavLinkClass(pathname, "/")}>
                    <NavLabel icon={LayoutDashboard} label="Dashboard" />
                  </Link>
                </li>
                <li>
                  <Link href="/alunos" className={getNavLinkClass(pathname, "/alunos")}>
                    <NavLabel icon={GraduationCap} label="Alunos" />
                  </Link>
                </li>
                <li>
                  <Link href="/colaboradores" className={getNavLinkClass(pathname, "/colaboradores")}>
                    <NavLabel icon={BriefcaseBusiness} label="Colaboradores" />
                  </Link>
                </li>
                <li>
                  <Link href="/atendimentos" className={getNavLinkClass(pathname, "/atendimentos")}>
                    <NavLabel icon={CalendarDays} label="Atendimentos" />
                  </Link>
                </li>
                {canAccessDespesas ? (
                  <li>
                    <Link href="/despesas" className={getNavLinkClass(pathname, "/despesas")}>
                      <NavLabel icon={Receipt} label="Despesas" />
                    </Link>
                  </li>
                ) : null}
                {isAdmin ? (
                  <li>
                    <Link href="/admin" className={getNavLinkClass(pathname, "/admin")}>
                      <NavLabel icon={ShieldUser} label="Admin" />
                    </Link>
                  </li>
                ) : null}

                <li className="mt-2 border-t border-base-300 pt-2">
                  <div className="pointer-events-none p-0">
                    <UserSummary user={user} />
                  </div>
                </li>

                <li className="mt-1 px-1">
                  <LogoutButton />
                </li>
              </ul>
            </div>

            <Link href="/" className="flex shrink-0 items-center" aria-label="Aprimorar">
              <Image src="/aprimorar_logo.svg" alt="Aprimorar" width={236} height={260} className="h-10 w-auto" priority />
            </Link>
          </div>

          <div className="navbar-center hidden min-w-0 flex-1 justify-center lg:flex">
            <ul className="menu menu-horizontal gap-2 px-1">
              <li>
                <Link href="/" className={getNavLinkClass(pathname, "/")}>
                  <NavLabel icon={LayoutDashboard} label="Dashboard" />
                </Link>
              </li>
              <li>
                <Link href="/alunos" className={getNavLinkClass(pathname, "/alunos")}>
                  <NavLabel icon={GraduationCap} label="Alunos" />
                </Link>
              </li>
              <li>
                <Link href="/colaboradores" className={getNavLinkClass(pathname, "/colaboradores")}>
                  <NavLabel icon={BriefcaseBusiness} label="Colaboradores" />
                </Link>
              </li>
              <li>
                <Link href="/atendimentos" className={getNavLinkClass(pathname, "/atendimentos")}>
                  <NavLabel icon={CalendarDays} label="Atendimentos" />
                </Link>
              </li>
              {canAccessDespesas ? (
                <li>
                  <Link href="/despesas" className={getNavLinkClass(pathname, "/despesas")}>
                    <NavLabel icon={Receipt} label="Despesas" />
                  </Link>
                </li>
              ) : null}
              {isAdmin ? (
                <li>
                  <Link href="/admin" className={getNavLinkClass(pathname, "/admin")}>
                    <NavLabel icon={ShieldUser} label="Admin" />
                  </Link>
                </li>
              ) : null}
            </ul>
          </div>

          <div className="navbar-end hidden w-auto items-center gap-3 lg:flex">
            <div className="text-right">
              <UserSummary user={user} />
            </div>
            <LogoutButton compact />
          </div>
        </div>
      </header>

      <main className="mx-auto w-full min-w-0 px-4 py-5 md:px-8 md:py-7">{children}</main>
    </div>
  );
}
