import { PageLayout } from "@/components/layout/PageLayout";
import { LoginPage } from "@/features/auth/pages/LoginPage";
import { ShieldCheck } from "lucide-react";

const headerProps = {
  description: "Playground para revisar fluxos administrativos e componentes de autenticação.",
  title: "Admin",
  Icon: ShieldCheck,
  backLink: "/",
};

export function AdminPage() {
  return (
    <PageLayout {...headerProps}>
      <div className="mb-4 rounded-2xl border border-warning/20 bg-warning/10 px-4 py-3 text-sm text-base-content/70">
        Esta tela está servindo como playground. O login abaixo ainda usa sessão mockada em localStorage.
      </div>

      <LoginPage />
    </PageLayout>
  );
}
