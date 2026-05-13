import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { DateRangeInput } from "@/components/ui/date-range-input";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { Pagination } from "@/components/ui/pagination";
import {
  getFinanceTransactionsQueryParamsCategoryEnum,
  getFinanceTransactionsQueryParamsStatusEnum,
  getFinanceTransactionsQueryParamsTypeEnum,
  type PagedModelTransactionResponseDTO,
  type TransactionResponseDTO,
  useGetFinanceSummary,
  useGetFinanceTransactions,
} from "@/kubb";
import { brl, formatDateShortYear } from "@/lib/utils/formatter";
import {
  ArrowDownRight,
  ArrowUpRight,
  Banknote,
  CalendarRange,
  CircleDollarSign,
  Landmark,
  ReceiptText,
  RotateCcw,
  Wallet,
} from "lucide-react";
import type { ReactNode } from "react";
import { useMemo, useState } from "react";

const categoryLabels = {
  COBRANCA_ALUNO: "Cobranca de aluno",
  PAGAMENTO_COLABORADOR: "Pagamento de colaborador",
  CONTAS: "Contas",
  ADMINISTRATIVO: "Administrativo",
  DESPENSA: "Despensa",
  MANUTENCAO: "Manutencao",
  SERVICOS: "Servicos",
  MATERIAIS: "Materiais",
  ASSINATURAS: "Assinaturas",
} as const;

const statusLabels = {
  PENDING: "Pendente",
  SETTLED: "Liquidado",
} as const;

const typeLabels = {
  IN: "Entrada",
  OUT: "Saida",
} as const;

const originLabels = {
  APPOINTMENT_STUDENT_CHARGE: "Atendimento • cobranca do aluno",
  APPOINTMENT_EMPLOYEE_PAYMENT: "Atendimento • repasse ao colaborador",
  GENERAL_EXPENSE: "Despesa geral",
} as const;

function toLocalDateString(date: Date | null) {
  if (!date) return undefined;

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
}

function normalizePagination(data?: PagedModelTransactionResponseDTO) {
  if (!data?.page) return undefined;

  return {
    size: data.page.size ?? 0,
    totalElements: data.page.totalElements ?? 0,
    totalPages: data.page.totalPages ?? 0,
    content: data.content ?? [],
  };
}

function getTransactionDate(transaction: TransactionResponseDTO) {
  if (transaction.date) return formatDateShortYear(transaction.date);
  if (transaction.settledAt) return formatDateShortYear(transaction.settledAt);
  return "Aguardando baixa";
}

function KpiCard({
  title,
  subtitle,
  value,
  tone,
  icon,
}: {
  title: string;
  subtitle: string;
  value: string;
  tone: "success" | "warning" | "primary" | "secondary";
  icon: ReactNode;
}) {
  const toneClasses = {
    success: "border-success/15 bg-linear-to-br from-success/10 via-base-100 to-base-100 text-success",
    warning: "border-warning/15 bg-linear-to-br from-warning/10 via-base-100 to-base-100 text-warning",
    primary: "border-primary/15 bg-linear-to-br from-primary/10 via-base-100 to-base-100 text-primary",
    secondary: "border-secondary/15 bg-linear-to-br from-secondary/10 via-base-100 to-base-100 text-secondary",
  } as const;

  return (
    <article className={`rounded-2xl border p-4 shadow-sm ${toneClasses[tone]}`}>
      <div className="mb-4 flex items-start justify-between gap-3">
        <div>
          <p className="text-[11px] font-bold uppercase tracking-[0.2em] text-base-content/45">{title}</p>
          <p className="mt-1 text-sm text-base-content/60">{subtitle}</p>
        </div>
        <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-base-100/85 shadow-sm">
          {icon}
        </div>
      </div>
      <p className="font-mono text-2xl font-bold text-base-content">{value}</p>
    </article>
  );
}

export function FinancesPage() {
  const [currentPage, setCurrentPage] = useState(0);
  const [selectedType, setSelectedType] = useState<"" | "IN" | "OUT">("");
  const [selectedStatus, setSelectedStatus] = useState<"" | "PENDING" | "SETTLED">("");
  const [selectedCategory, setSelectedCategory] = useState<
    ""
      | "COBRANCA_ALUNO"
      | "PAGAMENTO_COLABORADOR"
      | "CONTAS"
      | "ADMINISTRATIVO"
      | "DESPENSA"
      | "MANUTENCAO"
      | "SERVICOS"
      | "MATERIAIS"
      | "ASSINATURAS"
  >("");
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);

  const summaryQuery = useGetFinanceSummary();
  const transactionsQuery = useGetFinanceTransactions({
    page: currentPage,
    size: 12,
    sort: ["settledAt,desc", "id,desc"],
    type: selectedType || undefined,
    status: selectedStatus || undefined,
    category: selectedCategory || undefined,
    startDate: toLocalDateString(startDate),
    endDate: toLocalDateString(endDate),
  });

  const hasFilters = Boolean(selectedType || selectedStatus || selectedCategory || startDate || endDate);
  const transactions = transactionsQuery.data?.content ?? [];
  const paginationData = useMemo(() => normalizePagination(transactionsQuery.data), [transactionsQuery.data]);

  const incomeRealized = summaryQuery.data?.totalIncome ?? 0;
  const incomePending = summaryQuery.data?.totalIncomePending ?? 0;
  const employeeExpense = summaryQuery.data?.totalExpenseTeacher ?? 0;
  const employeeExpensePending = summaryQuery.data?.totalExpenseTeacherPending ?? 0;
  const generalExpenses = summaryQuery.data?.totalGeneralExpenses ?? 0;
  const currentBalance = summaryQuery.data?.balance ?? 0;
  const totalCommittedExpense = employeeExpense + employeeExpensePending + generalExpenses;

  const handleClearFilters = () => {
    setSelectedType("");
    setSelectedStatus("");
    setSelectedCategory("");
    setStartDate(null);
    setEndDate(null);
    setCurrentPage(0);
  };

  const headerProps = {
    description: "Acompanhe o panorama financeiro da instituicao com foco em fluxo, pendencias e movimentacoes.",
    title: "Financeiro",
    Icon: Landmark,
    backLink: "/",
  };

  if (summaryQuery.isPending && transactionsQuery.isPending) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando panorama financeiro" />
      </PageLayout>
    );
  }

  if (summaryQuery.isError && transactionsQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard title="Nao foi possivel carregar o financeiro" error={summaryQuery.error} />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">
        <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_200ms_ease-out_both]">
          <div className="flex flex-col gap-5 lg:flex-row lg:items-start lg:justify-between">
            <div className="space-y-3">
              <div className="flex flex-wrap items-center gap-2">
                <span className="badge badge-primary badge-outline px-3 py-3">Resumo institucional</span>
                <span className="badge badge-ghost px-3 py-3">Baseado nas transacoes registradas</span>
              </div>
              <div>
                <h2 className="text-2xl font-bold text-base-content">Panorama do caixa e das obrigacoes financeiras</h2>
                <p className="mt-1 max-w-3xl text-sm leading-6 text-base-content/60">
                  Esta visao inicial combina receitas, pendencias e despesas ja cadastradas para orientar as proximas decisoes do fluxo financeiro.
                </p>
              </div>
            </div>

            <div className="rounded-2xl border border-primary/15 bg-linear-to-br from-primary/8 via-base-100 to-base-100 px-4 py-3 shadow-sm">
              <p className="text-[11px] font-bold uppercase tracking-[0.22em] text-base-content/45">Saldo consolidado</p>
              <p className={`mt-2 font-mono text-3xl font-bold ${currentBalance >= 0 ? "text-success" : "text-error"}`}>
                {brl.format(currentBalance)}
              </p>
              <p className="mt-2 text-xs text-base-content/55">
                Receita realizada menos despesas pagas e despesas gerais baixadas.
              </p>
            </div>
          </div>
        </section>

        {summaryQuery.isError ? (
          <ErrorCard title="Nao foi possivel carregar o resumo financeiro" error={summaryQuery.error} />
        ) : (
          <section className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3 animate-[fade-up_280ms_ease-out_both]">
            <KpiCard
              title="Receita realizada"
              subtitle="Valores ja recebidos dos alunos"
              value={brl.format(incomeRealized)}
              tone="success"
              icon={<ArrowDownRight className="h-5 w-5" />}
            />
            <KpiCard
              title="A receber"
              subtitle="Cobrancas ainda pendentes"
              value={brl.format(incomePending)}
              tone="warning"
              icon={<Wallet className="h-5 w-5" />}
            />
            <KpiCard
              title="Repasses pagos"
              subtitle="Despesas liquidadas com colaboradores"
              value={brl.format(employeeExpense)}
              tone="primary"
              icon={<ArrowUpRight className="h-5 w-5" />}
            />
            <KpiCard
              title="A pagar"
              subtitle="Repasses ainda nao liquidados"
              value={brl.format(employeeExpensePending)}
              tone="warning"
              icon={<CircleDollarSign className="h-5 w-5" />}
            />
            <KpiCard
              title="Despesas gerais"
              subtitle="Custos administrativos e operacionais"
              value={brl.format(generalExpenses)}
              tone="secondary"
              icon={<ReceiptText className="h-5 w-5" />}
            />
            <KpiCard
              title="Compromisso total"
              subtitle="Pagos + pendentes ja registrados"
              value={brl.format(totalCommittedExpense)}
              tone="primary"
              icon={<Banknote className="h-5 w-5" />}
            />
          </section>
        )}

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
            <div className="space-y-1">
              <h3 className="text-lg font-bold text-base-content">Filtros de movimentacao</h3>
              <p className="text-sm text-base-content/60">
                Refine a listagem por periodo, natureza da transacao e status financeiro.
              </p>
            </div>

            {hasFilters ? (
              <Button variant="ghost" className="gap-2 lg:self-start" onClick={handleClearFilters}>
                <RotateCcw className="h-4 w-4" />
                Limpar filtros
              </Button>
            ) : null}
          </div>

          <div className="mt-4 flex flex-col gap-3 xl:flex-row xl:items-center xl:justify-between">
            <div className="grid flex-1 gap-3 md:grid-cols-3">
              <label className="flex flex-col gap-2">
                <span className="text-xs font-bold uppercase tracking-[0.16em] text-base-content/45">Tipo</span>
                <select
                  className="select rounded-2xl border-base-300 bg-base-100 shadow-sm"
                  value={selectedType}
                  onChange={(event) => {
                    setSelectedType(event.target.value as typeof selectedType);
                    setCurrentPage(0);
                  }}
                >
                  <option value="">Todos</option>
                  <option value={getFinanceTransactionsQueryParamsTypeEnum.IN}>Entradas</option>
                  <option value={getFinanceTransactionsQueryParamsTypeEnum.OUT}>Saidas</option>
                </select>
              </label>

              <label className="flex flex-col gap-2">
                <span className="text-xs font-bold uppercase tracking-[0.16em] text-base-content/45">Status</span>
                <select
                  className="select rounded-2xl border-base-300 bg-base-100 shadow-sm"
                  value={selectedStatus}
                  onChange={(event) => {
                    setSelectedStatus(event.target.value as typeof selectedStatus);
                    setCurrentPage(0);
                  }}
                >
                  <option value="">Todos</option>
                  <option value={getFinanceTransactionsQueryParamsStatusEnum.PENDING}>Pendentes</option>
                  <option value={getFinanceTransactionsQueryParamsStatusEnum.SETTLED}>Liquidados</option>
                </select>
              </label>

              <label className="flex flex-col gap-2">
                <span className="text-xs font-bold uppercase tracking-[0.16em] text-base-content/45">Categoria</span>
                <select
                  className="select rounded-2xl border-base-300 bg-base-100 shadow-sm"
                  value={selectedCategory}
                  onChange={(event) => {
                    setSelectedCategory(event.target.value as typeof selectedCategory);
                    setCurrentPage(0);
                  }}
                >
                  <option value="">Todas</option>
                  {Object.entries(getFinanceTransactionsQueryParamsCategoryEnum).map(([, value]) => (
                    <option key={value} value={value}>
                      {categoryLabels[value]}
                    </option>
                  ))}
                </select>
              </label>
            </div>

            <div className="xl:min-w-fit">
              <DateRangeInput
                startDate={startDate}
                endDate={endDate}
                onStartDateChange={(date) => {
                  setStartDate(date);
                  setCurrentPage(0);
                }}
                onEndDateChange={(date) => {
                  setEndDate(date);
                  setCurrentPage(0);
                }}
              />
            </div>
          </div>
        </section>

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_380ms_ease-out_both]">
          <div className="mb-4 flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
            <div>
              <div className="flex items-center gap-2">
                <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-primary/10 text-primary shadow-sm">
                  <CalendarRange className="h-5 w-5" />
                </div>
                <div>
                  <h3 className="text-lg font-bold text-base-content">Movimentacoes registradas</h3>
                  <p className="text-sm text-base-content/60">
                    Lista consolidada de entradas, repasses e despesas gerais registradas no sistema.
                  </p>
                </div>
              </div>
            </div>

            <div className="badge badge-outline badge-primary badge-lg font-semibold">
              {paginationData?.totalElements ?? transactions.length} registros
            </div>
          </div>

          {transactionsQuery.isError ? (
            <ErrorCard title="Nao foi possivel carregar as movimentacoes" error={transactionsQuery.error} />
          ) : transactionsQuery.isPending ? (
            <LoadingCard title="Carregando movimentacoes" />
          ) : transactions.length === 0 ? (
            <EmptyCard
              title="Nenhuma movimentacao encontrada"
              description="Ajuste os filtros para localizar cobrancas, repasses e despesas ja registradas."
            />
          ) : (
            <>
              <div className="hidden md:block">
                <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
                  <table className="table table-zebra w-full bg-base-100">
                    <thead className="bg-base-200/80">
                      <tr>
                        <th className="font-bold text-base-content/70">Data</th>
                        <th className="font-bold text-base-content/70">Categoria</th>
                        <th className="font-bold text-base-content/70">Origem</th>
                        <th className="font-bold text-base-content/70">Tipo</th>
                        <th className="font-bold text-base-content/70">Status</th>
                        <th className="text-right font-bold text-base-content/70">Valor</th>
                      </tr>
                    </thead>
                    <tbody>
                      {transactions.map((transaction) => {
                        const isIncome = transaction.type === "IN";
                        const tone = isIncome ? "text-success" : "text-error";
                        const sign = isIncome ? "+" : "-";

                        return (
                          <tr key={transaction.id} className="transition-colors hover:bg-base-200/45">
                            <td className="text-sm font-medium text-base-content/70">{getTransactionDate(transaction)}</td>
                            <td>
                              <div className="font-semibold text-base-content">
                                {transaction.category ? categoryLabels[transaction.category] : "Sem categoria"}
                              </div>
                            </td>
                            <td>
                              <div className="text-sm text-base-content/65">
                                {transaction.origin ? originLabels[transaction.origin] : "Origem nao identificada"}
                              </div>
                            </td>
                            <td>
                              <span className={`badge badge-outline ${isIncome ? "badge-success" : "badge-error"}`}>
                                {transaction.type ? typeLabels[transaction.type] : "-"}
                              </span>
                            </td>
                            <td>
                              <span className={`badge ${transaction.status === "SETTLED" ? "badge-success" : "badge-warning"}`}>
                                {transaction.status ? statusLabels[transaction.status] : "-"}
                              </span>
                            </td>
                            <td className={`text-right font-mono text-sm font-bold ${tone}`}>
                              {sign} {brl.format(transaction.amount ?? 0)}
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </div>

              <div className="flex flex-col gap-3 md:hidden">
                {transactions.map((transaction) => {
                  const isIncome = transaction.type === "IN";

                  return (
                    <article key={transaction.id} className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm">
                      <div className="flex items-start justify-between gap-3">
                        <div>
                          <p className="text-[11px] font-bold uppercase tracking-[0.18em] text-base-content/45">
                            {transaction.category ? categoryLabels[transaction.category] : "Sem categoria"}
                          </p>
                          <p className="mt-1 text-sm text-base-content/65">
                            {transaction.origin ? originLabels[transaction.origin] : "Origem nao identificada"}
                          </p>
                        </div>
                        <div className={`font-mono text-lg font-bold ${isIncome ? "text-success" : "text-error"}`}>
                          {isIncome ? "+" : "-"} {brl.format(transaction.amount ?? 0)}
                        </div>
                      </div>

                      <div className="mt-4 grid grid-cols-2 gap-2 text-sm">
                        <div className="rounded-xl bg-base-200/50 px-3 py-2">
                          <p className="text-[11px] font-bold uppercase tracking-[0.14em] text-base-content/45">Data</p>
                          <p className="mt-1 font-medium text-base-content">{getTransactionDate(transaction)}</p>
                        </div>
                        <div className="rounded-xl bg-base-200/50 px-3 py-2">
                          <p className="text-[11px] font-bold uppercase tracking-[0.14em] text-base-content/45">Status</p>
                          <p className="mt-1 font-medium text-base-content">
                            {transaction.status ? statusLabels[transaction.status] : "-"}
                          </p>
                        </div>
                      </div>
                    </article>
                  );
                })}
              </div>

              <Pagination
                paginationData={paginationData}
                currentPage={currentPage}
                onPageChange={setCurrentPage}
              />
            </>
          )}
        </section>
      </div>
    </PageLayout>
  );
}
