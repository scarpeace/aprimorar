import { Plus, type LucideIcon } from "lucide-react";
import { Suspense, useState, type PropsWithChildren, type ReactNode } from "react";
import { Button } from "../ui/button";
import { Modal } from "@/components/ui/modal";
import { AtendimentoForm } from "@/features/atendimentos/components/AtendimentoForm";
import { AlunoForm } from "@/features/alunos/components/AlunoForm";

const ICON_BG_VARIANTS = {
  primary: "bg-primary/30 text-primary",
  secondary: "bg-secondary/30 text-secondary",
  accent: "bg-accent/30 text-accent",
  neutral: "bg-neutral/20 text-neutral",
  info: "bg-info/25 text-info",
  success: "bg-success/25 text-success",
  warning: "bg-warning/25 text-warning",
  error: "bg-error/25 text-error",
} as const;

type IconBgVariant = keyof typeof ICON_BG_VARIANTS;

type PageLayoutProps = {
  title: string;
  description: string;
  Icon: LucideIcon;
  children: ReactNode;
  iconBg: IconBgVariant;
};

export function PageLayout({
  title,
  description,
  Icon,
  children,
  iconBg,
}: Readonly<PropsWithChildren<PageLayoutProps>>) {
  const iconBgClass = ICON_BG_VARIANTS[iconBg] ?? ICON_BG_VARIANTS.primary;
  const [isAtendimentoFormOpen, setIsAtendimentoFormOpen] = useState(false);
  const [isAlunoFormOpen, setIsAlunoFormOpen] = useState(false);

  return (
    <>
      <header className="flex justify-between flex-col items-start gap-4 min-w-0 mb-6 sm:flex-row sm:items-center">
        <div className="flex w-full items-center justify-between ">
          <div className="ml-3 flex min-w-0 flex-row justify-center gap-0.5 sm:gap-1">
            <div className={`shrink-0 rounded-full p-3 mr-3 ${iconBgClass}`}>
              <Icon size={48} />
            </div>
            <div>
              <h1 className="truncate text-2xl font-bold text-base-content sm:text-3xl">
                {title}
              </h1>
              <p className="line-clamp-2 text-xs text-base-content/70 sm:text-sm">
                {description}
              </p>
            </div>
          </div>

          <div className="flex gap-3">
          <Button variant="success" onClick={() => setIsAtendimentoFormOpen(true)}><Plus className="mr-2 h-4 w-4" />Novo atendimento</Button>
          </div>
        </div>
      </header>
      {children}

      <Modal
        isOpen={isAtendimentoFormOpen}
        onClose={() => setIsAtendimentoFormOpen(false)}
        title="Cadastrar Novo Atendimento"
        description="Defina aluno, colaborador, horario e valores do atendimento para manter agenda e financeiro sincronizados."
        size="lg"
      >
        <Suspense fallback={<p className="text-sm text-base-content/60">Carregando formulário...</p>}>
          <AtendimentoForm
            initialData={null}
            onSuccess={() => setIsAtendimentoFormOpen(false)}
            onCancel={() => setIsAtendimentoFormOpen(false)}
          />
        </Suspense>
      </Modal>
    </>
  );
}
