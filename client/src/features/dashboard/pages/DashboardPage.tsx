import { AtendimentosCalendar } from "../components/AtendimentosCalendar";
import { lazy, Suspense, useState } from "react";

const AtendimentoForm = lazy(() => import("@/features/appointments/components/AtendimentoForm").then((module) => ({ default: module.AtendimentoForm })));

export function DashboardPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);

  const handleCloseForm = () => {
    setIsFormOpen(false);
  };

  return (
    <>
      <section className="flex flex-col gap-7">
        <h1 className="text-3xl font-bold text-base-content">Painel</h1>
        <AtendimentosCalendar onCreateAppointment={() => setIsFormOpen(true)} />
      </section>

      {isFormOpen ? (
        <div className="modal modal-open">
          <div className="modal-box max-w-4xl border border-base-300 bg-base-100 shadow-2xl">
            <h3 className="mb-1 text-lg font-bold">Cadastrar Novo Atendimento</h3>
            <p className="mb-4 text-sm text-base-content/60">
              Defina aluno, colaborador, horario e valores do atendimento para manter agenda e financeiro sincronizados.
            </p>
            <Suspense fallback={<p className="text-sm text-base-content/60">Carregando formulário...</p>}>
              <AtendimentoForm
                initialData={null}
                onSuccess={handleCloseForm}
                onCancel={handleCloseForm}
              />
            </Suspense>
          </div>
        </div>
      ) : null}
    </>
  );
}
