import {usePageDateFilter} from "@/hooks/usePageDateFilter.ts";
import {Suspense, useState} from "react";
import {HandCoins, UserCheck, UserCircle} from "lucide-react";
import {PageLayout} from "@/components/Layout/PageLayout.tsx";
import {Modal} from "@/components/Ui/Modal.tsx";
import {KpiCard} from "@/components/Ui/KpiCard.tsx";
import {AlunoForm} from "@/components/Aluno/AlunoForm.tsx";
import { DateRangeSelectWidget } from "@/components/Ui/DateRangeSelectWidget";
import { TransacoesTable } from "@/components/Financeiro/TransacoesTable";
import { transacaoResponseDTOTipoEnum } from "@/kubb";

export function FinanceiroPage(){
    const { startDate, endDate, ...dateFilter } = usePageDateFilter();
    const [isFormOpen, setIsFormOpen] = useState(false);

    const headerProps = {
        description: "Gerencie custos e despesas.",
        title: "Financeiro",
        Icon: HandCoins,
        iconBg: "accent",
    } as const;

    return (
        <PageLayout {...headerProps}>

            <section className="mb-3 rounded-2xl bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
                <div className="flex flex-row justify-between items-center gap-3">
                    <div>
                        <h3 className="text-2xl font-bold text-base-content">Resumo Financeiro</h3>
                        <p className="text-sm text-base-content/60">
                            Visão geral das finanças e despesas da instituição.
                        </p>
                    </div>

                    <div className="flex gap-3">
                        <KpiCard
                            label="Total Entrada"
                            value={10}
                            Icon={UserCheck}
                            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
                        />
                        <KpiCard
                            label="Total Saída"
                            value={20}
                            Icon={UserCircle}
                            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
                        />
                    </div>
                </div>
            </section>

            <section className="grid grid-cols-2 gap-6 rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both] 2xl:grid-cols-2">
          <div className="min-w-0 rounded-2xl border border-base-300 p-3">
            <TransacoesTable
              title="Entradas"
              tipo={transacaoResponseDTOTipoEnum.ENTRADA}
              openForm={() => setIsFormOpen(true)}
            />
          </div>

          <div className="min-w-0 rounded-2xl border border-base-300 p-3">
            <TransacoesTable
              title="Saídas"
              tipo={transacaoResponseDTOTipoEnum.SAIDA}
              openForm={() => setIsFormOpen(true)}
            />
          </div>
        </section>

            <DateRangeSelectWidget startDate={startDate} endDate={endDate} {...dateFilter} />

            <Modal
                isOpen={isFormOpen}
                onClose={() => setIsFormOpen(false)}
                title="Cadastrar Novo Aluno"
                description="Informe os dados do aluno para efetuar a matrícula, um aluno deve ter um responsável no momento do seu cadastro."
                size="lg"
            >
                <Suspense fallback={<p className="text-sm text-base-content/60">Carregando formulário...</p>}>
                    <AlunoForm
                        onSuccess={() => setIsFormOpen(false)}
                        onCancel={() => setIsFormOpen(false)}
                    />
                </Suspense>
            </Modal>

        </PageLayout>
    );
}
