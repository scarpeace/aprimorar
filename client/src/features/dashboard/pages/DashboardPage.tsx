import { PageLayout } from "@/components/layout/PageLayout";
import { AtendimentosCalendar } from "../components/AtendimentosCalendar";
import { CalendarDays } from "lucide-react";


export function DashboardPage() {

  const headerProps = {
    description: "Visão geral da operação do Aprimorar",
    title: "Dashboard",
    Icon: CalendarDays,
    backLink: "/",
    iconBg: "primary",
  } as const;


  return (
    <>
      <PageLayout {...headerProps}>
        <AtendimentosCalendar />
      </PageLayout>
      </>
  );
}
