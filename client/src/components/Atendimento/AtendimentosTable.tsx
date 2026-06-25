import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { LoadingSpinner } from "@/components/Ui/LoadingSpinner.tsx";
import { Pagination } from "@/components/Ui/Pagination.tsx";
import { TransacaoFilterSelect } from "@/components/Financeiro/TransacaoFilterSelect";
import {
  atendimentoResponseStatusEnum,
  atendimentoResponseTipoEnum,
  useGetAtendimentos,
  type AtendimentoResponse,
  type AtendimentoResponseStatusEnumKey,
  type AtendimentoResponseTipoEnumKey,
} from "@/kubb";
import { brl } from "@/utils/formatter.ts";
import { formatDateShortYear, formatTime } from "@/utils/date-utils.ts";
import { useNavigate } from "react-router-dom";
import { tipoAtendimentoLabels } from "@/utils/constants/atendimento-constants.ts";
import { AtendimentoMobileCard } from "./AtendimentoMobileCard.tsx";
import { Button } from "@/components/Ui/Button.tsx";
import { Plus } from "lucide-react";
import { TextSearchInput } from "@/components/Ui/TextSearchInput.tsx";
import { useDebounce } from "@/hooks/useDebounce.ts";
import { usePageDateFilter } from "@/hooks/usePageDateFilter.ts";
import { useState } from "react";

type AtendimentosTableProps = {
  openForm: () => void;
};

const statusOptions = [
  { value: atendimentoResponseStatusEnum.AGENDADO, label: "Agendados" },
  { value: atendimentoResponseStatusEnum.CONCLUIDO, label: "Concluídos" },
  { value: atendimentoResponseStatusEnum.CANCELADO, label: "Cancelados" },
] as const;

const tipoOptions = [
  { value: atendimentoResponseTipoEnum.AULA, label: tipoAtendimentoLabels.AULA },
  { value: atendimentoResponseTipoEnum.MENTORIA, label: tipoAtendimentoLabels.MENTORIA },
  { value: atendimentoResponseTipoEnum.TERAPIA, label: tipoAtendimentoLabels.TERAPIA },
  { value: atendimentoResponseTipoEnum.ORIENTACAO_VOCACIONAL, label: tipoAtendimentoLabels.ORIENTACAO_VOCACIONAL },
  { value: atendimentoResponseTipoEnum.ENEM, label: tipoAtendimentoLabels.ENEM },
  { value: atendimentoResponseTipoEnum.PAS, label: tipoAtendimentoLabels.PAS },
  { value: atendimentoResponseTipoEnum.OUTRO, label: tipoAtendimentoLabels.OUTRO },
] as const;

function formatDateInputValue(date?: Date) {
  if (!date) {
    return "";
  }

  const year = date.getFullYear();
  const month = `${date.getMonth() + 1}`.padStart(2, "0");
  const day = `${date.getDate()}`.padStart(2, "0");

  return `${year}-${month}-${day}`;
}

export function AtendimentosTable({ openForm }: AtendimentosTableProps) {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [filterStatus, setFilterStatus] = useState<AtendimentoResponseStatusEnumKey | "">("");
  const [filterTipo, setFilterTipo] = useState<AtendimentoResponseTipoEnumKey | "">("");
  const {
    startDate,
    endDate,
    handleStartDateChange,
    handleEndDateChange,
  } = usePageDateFilter();
  const debouncedSearch = useDebounce(search, 500);

  const eventsQuery = useGetAtendimentos({
    page,
    size: 20,
    inicio: startDate?.toISOString(),
    fim: endDate?.toISOString(),
    sort: ["inicio,desc", "id,asc"],
    busca: debouncedSearch || undefined,
    status: filterStatus || undefined,
    tipo: filterTipo || undefined,
  });

  const events = eventsQuery.data?.content ?? [];
  const pagination = eventsQuery.data?.page;
  const hasEvents = events.length > 0;

  return (
    <>

      <section className="flex flex-col gap-3 my-3 animate-[fade-up_220ms_ease-out_both] lg:flex-row lg:items-center lg:justify-between">
        <div>
          <h3 className="text-2xl font-bold text-base-content">Atendimentos registrados</h3>
          <p className="text-sm text-base-content/60">
            Selecione um atendimento para visualizar os detalhes.
          </p>
        </div>

        <div className="flex items-end gap-3">
          <TextSearchInput
            label="Pesquisar"
            className="w-120"
            placeholder="Aluno, colaborador, ou tipo do atendimento"
            onChange={(value) => {
              setSearch(value);
              setPage(0);
            }}
          />

          <fieldset className="fieldset w-40 shrink-0">
            <legend className="fieldset-legend">Data inicial</legend>
            <input
              type="date"
              className="input input-bordered w-full"
              value={formatDateInputValue(startDate)}
              onChange={(event) => {
                handleStartDateChange(event.target.value ? new Date(`${event.target.value}T00:00:00`) : null);
                setPage(0);
              }}
            />
          </fieldset>

          <fieldset className="fieldset w-40 shrink-0">
            <legend className="fieldset-legend">Data final</legend>
            <input
              type="date"
              className="input input-bordered w-full"
              value={formatDateInputValue(endDate)}
              onChange={(event) => {
                handleEndDateChange(event.target.value ? new Date(`${event.target.value}T23:59:59.999`) : null);
                setPage(0);
              }}
            />
          </fieldset>

          <TransacaoFilterSelect
            label="Status"
            value={filterStatus}
            placeholder="Todos os status"
            options={statusOptions}
            onChange={(value) => {
              setFilterStatus(value as AtendimentoResponseStatusEnumKey | "");
              setPage(0);
            }}
          />

          <TransacaoFilterSelect
            label="Tipo"
            value={filterTipo}
            placeholder="Todos os tipos"
            options={tipoOptions}
            onChange={(value) => {
              setFilterTipo(value as AtendimentoResponseTipoEnumKey | "");
              setPage(0);
            }}
          />

          <Button variant="success" onClick={() => openForm()}><Plus className="mr-2 h-4 w-4" />Novo atendimento</Button>
        </div>
      </section>

      {eventsQuery.isError && (
        <ErrorCard title="Não foi possível carregar a listagem de atendimentos" error={eventsQuery.error} />
      )}

      {eventsQuery.isLoading && (
        <LoadingSpinner text="Carregando atendimentos..." />
      )}

      {!eventsQuery.isLoading && !eventsQuery.isError && !hasEvents && (
        <span>CRIAR UM NOVO CARD PRA COLOCAR AQUI</span>
      )}

      {hasEvents && (
        <>
          <div className="hidden md:block">
            <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
              <table className="table table-zebra w-full table-auto bg-base-100">
                <thead className="sticky z-10 bg-base-200/90">
                  <tr>
                    <th className="font-bold text-base-content/70">Aluno</th>
                    <th className="font-bold text-base-content/70">Colaborador</th>
                    <th className="font-bold text-base-content/70">Data</th>
                    <th className="text-center font-bold text-base-content/70">Horário</th>
                    <th className="text-center font-bold text-base-content/70">Tipo</th>
                    <th className="text-center font-bold text-base-content/70">Status</th>
                    <th className="text-right font-bold text-base-content/70">Valor</th>
                    <th className="text-right font-bold text-base-content/70">Repasse</th>
                  </tr>
                </thead>

                <tbody className="whitespace-nowrap">
                  {events.map((atendimento: AtendimentoResponse) => (
                    <tr
                      key={atendimento.id}
                      className="group cursor-pointer transition-colors hover:bg-base-200/50"
                      onClick={() => navigate(`/atendimentos/${atendimento.id}`)}
                    >
                      <td>
                        <div className="font-semibold text-base-content">{atendimento.nomeAluno}</div>
                      </td>
                      <td>{atendimento.nomeColaborador}</td>
                      <td>{formatDateShortYear(atendimento.inicio)}</td>
                      <td className="text-center text-sm font-medium">
                        {formatTime(atendimento.inicio)} - {formatTime(atendimento.fim)}
                      </td>
                      <td className="text-center">
                        <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                          {tipoAtendimentoLabels[atendimento.tipo] ?? atendimento.tipo}
                        </span>
                      </td>
                      <td className="text-center">
                        <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                          {atendimento.status}
                        </span>
                      </td>
                      <td className="text-right">
                        <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                          <span>{brl.format(atendimento.valor)}</span>
                        </div>
                      </td>
                      <td className="text-right">
                        <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                          <span>{brl.format(atendimento.repasse)}</span>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div className="flex flex-col gap-4 md:hidden">
            {events.map((atendimento: AtendimentoResponse, index: number) => (
              <AtendimentoMobileCard
                key={atendimento.id}
                atendimento={atendimento}
                alunoNome={atendimento.nomeAluno}
                colaboradorNome={atendimento.nomeColaborador}
                index={index}
              />
            ))}
          </div>
        </>
      )}

      <Pagination
        size={pagination?.size ?? 0}
        totalElements={pagination?.totalElements ?? 0}
        totalPages={pagination?.totalPages ?? 0}
        currentPage={page}
        onPageChange={(page) => { setPage(page); }}
      />
    </>
  );
}
