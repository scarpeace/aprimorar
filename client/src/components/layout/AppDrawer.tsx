"use client";

import Image from "next/image";
import Link from "next/link";
import { Menu } from "lucide-react";
import type { ReactNode } from "react";
import { useState } from "react";
import { AppSidebar } from "@/components/layout/AppSidebar";

type AppDrawerProps = {
  children: ReactNode;
};

export function AppDrawer({ children }: Readonly<AppDrawerProps>) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);

  function closeDrawer() {
    setIsDrawerOpen(false);
  }

  return (
    <div className="drawer min-h-screen lg:drawer-open">
      <input
        id="app-drawer"
        type="checkbox"
        className="drawer-toggle"
        checked={isDrawerOpen}
        onChange={(event) => setIsDrawerOpen(event.target.checked)}
      />

      <div className="drawer-content flex min-w-0 flex-col bg-base-200">
        <header className="navbar border-b border-base-300 bg-base-100 px-4 shadow-sm lg:hidden">
          <div className="navbar-start">
            <label htmlFor="app-drawer" className="btn btn-square btn-ghost" aria-label="Abrir navegação">
              <Menu size={20} />
            </label>
          </div>

          <div className="navbar-center">
            <Link href="/" aria-label="Aprimorar">
              <Image src="/aprimorar_logo.svg" alt="Aprimorar" width={236} height={260} className="h-9 w-auto" priority />
            </Link>
          </div>

          <div className="navbar-end" />
        </header>

        <main className="min-w-0 flex-1 p-4 md:p-6">
          <div className="mx-auto w-full max-w-7xl">{children}</div>
        </main>
      </div>

      <div className="drawer-side z-40">
        <label htmlFor="app-drawer" className="drawer-overlay" aria-label="Fechar navegação" />
        <AppSidebar onNavigate={closeDrawer} />
      </div>
    </div>
  );
}
