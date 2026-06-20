import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { AtendimentosCalendar } from "../../components/Dashboard/AtendimentosCalendar.tsx";
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
