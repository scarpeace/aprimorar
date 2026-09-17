import {
  BriefcaseBusiness,
  CalendarDays,
  GraduationCap,
  LayoutDashboard,
  ShieldUser,
  WalletCards,
  type LucideIcon,
} from "lucide-react";

type NavigationItem = {
  href: string;
  label: string;
  icon: LucideIcon;
  adminOnly?: boolean;
};

export const navigationItems: NavigationItem[] = [
  { href: "/", label: "Dashboard", icon: LayoutDashboard },
  { href: "/alunos", label: "Alunos", icon: GraduationCap },
  { href: "/colaboradores", label: "Colaboradores", icon: BriefcaseBusiness },
  { href: "/atendimentos", label: "Atendimentos", icon: CalendarDays },
  { href: "/financeiro", label: "Financeiro", icon: WalletCards },
  { href: "/admin", label: "Admin", icon: ShieldUser, adminOnly: true },
];
