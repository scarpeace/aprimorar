"use client";

import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { LogoutButton } from "@/auth/components/LogoutButton";
import { navigationItems } from "@/components/layout/navigation-items";

type AppSidebarProps = {
  onNavigate: () => void;
};

function isActive(pathname: string, href: string) {
  if (href === "/") {
    return pathname === href;
  }

  return pathname === href || pathname.startsWith(`${href}/`);
}

export function AppSidebar({ onNavigate }: Readonly<AppSidebarProps>) {
  const pathname = usePathname();

  return (
    <aside className="flex min-h-full w-72 flex-col border-r border-base-300 bg-base-100 p-4">
      <Link href="/" className="mb-6 flex items-center px-2" aria-label="Aprimorar" onClick={onNavigate}>
        <Image src="/aprimorar_logo.svg" alt="Aprimorar" width={236} height={260} className="h-10 w-auto" priority />
      </Link>

      <nav className="flex-1" aria-label="Navegação principal">
        <ul className="menu w-full gap-1 p-0">
          {navigationItems.map(({ href, label, icon: Icon }) => (
            <li key={href}>
              <Link href={href} className={isActive(pathname, href) ? "menu-active" : undefined} onClick={onNavigate}>
                <Icon size={18} />
                {label}
              </Link>
            </li>
          ))}
        </ul>
      </nav>

      <div className="mt-4 border-t border-base-300 pt-4">
        <LogoutButton />
      </div>
    </aside>
  );
}
