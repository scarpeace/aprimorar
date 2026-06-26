import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { BellElectric } from "lucide-react";
import { AtendimentosTable } from "../../components/Atendimento/AtendimentosTable.tsx";
import { Modal } from "@/components/Ui/Modal.tsx";
import { Suspense, useState } from "react";
import { AtendimentoForm } from "../../components/Atendimento/AtendimentoForm.tsx";

export function AtendimentosPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);

  const headerProps = {
    description: "Gerencie aulas e Atendimento.",
    title: "Atendimento",
    Icon: BellElectric,
    iconBg: "accent",
  } as const;


  return (
    <PageLayout {...headerProps}>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
        <AtendimentosTable openForm={() => setIsFormOpen(true)} />
      </section>


      <Modal
        isOpen={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        title="Cadastrar Novo Atendimento"
        description="Defina aluno, colaborador, horario e valores do atendimento para manter agenda e financeiro sincronizados."
        size="lg"
      >
        <Suspense fallback={<p className="text-sm text-base-content/60">Carregando formulário...</p>}>
          <AtendimentoForm
            initialData={null}
            onSuccess={() => setIsFormOpen(false)}
            onCancel={() => setIsFormOpen(false)}
          />
        </Suspense>
      </Modal>

    </PageLayout>
  );
}
