import type { ReactNode } from "react";
import { MainLayout } from "@/components/layout/MainLayout";
import { requireSession } from "@/lib/auth/session";

export default async function PrivateLayout({ children }: Readonly<{ children: ReactNode }>) {
  const session = await requireSession();

  return <MainLayout user={session.user}>{children}</MainLayout>;
}
