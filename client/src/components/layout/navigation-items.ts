import {
  BriefcaseBusiness,
  CalendarDays,
  GraduationCap,
  LayoutDashboard,
  Receipt,
  ShieldUser,
  type LucideIcon,
} from "lucide-react";

type NavigationItem = {
  href: string;
  label: string;
  icon: LucideIcon;
};

export const navigationItems: NavigationItem[] = [
  { href: "/", label: "Dashboard", icon: LayoutDashboard },
  { href: "/alunos", label: "Alunos", icon: GraduationCap },
  { href: "/colaboradores", label: "Colaboradores", icon: BriefcaseBusiness },
  { href: "/atendimentos", label: "Atendimentos", icon: CalendarDays },
  { href: "/despesas", label: "Despesas", icon: Receipt },
  { href: "/admin", label: "Admin", icon: ShieldUser },
];
