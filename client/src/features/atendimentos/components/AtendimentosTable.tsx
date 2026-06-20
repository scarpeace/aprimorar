import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { useGetAtendimentos, type AtendimentoResponse } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";
import { tipoAtendimentoLabels } from "../lib/tipo-atendimento-labels";
import { AtendimentoMobileCard } from "./AtendimentoMobileCard";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { TextSearchInput } from "@/components/ui/TextSearchInput";
import { useDebounce } from "@/lib/shared/use-debounce";
import { useState } from "react";

type AtendimentosTableProps = {
  openForm: () => void;
};

export function AtendimentosTable({ openForm }: AtendimentosTableProps) {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [startDate, setStartDate] = useState<Date | null>(new Date());
  const [endDate, setEndDate] = useState<Date | null>(null);
  const debouncedSearch = useDebounce(search, 500);

  const eventsQuery = useGetAtendimentos({
    page,
    size: 20,
    inicio: startDate?.toISOString(),
    fim: endDate?.toISOString(),
    sort: ["inicio,desc", "id,asc"],
    busca: debouncedSearch || undefined,
  })

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
          <TextSearchInput className="w-120" placeholder="Aluno, colaborador, ou tipo do atendimento" onChange={(value) => setSearch(value)} />
          <Button variant="success" onClick={() => openForm()}><Plus className="mr-2 h-4 w-4" />Novo atendimento</Button>
      </section>

      {eventsQuery.isError && (
        <ErrorCard title="Não foi possível carregar a listagem de atendimentos" error={eventsQuery.error} />
      )}

      {eventsQuery.isLoading && (
        <LoadingSpinner text="Carregando atendimentos..." />
      )}

      {!eventsQuery.isLoading && !eventsQuery.isError && !hasEvents && (
        <EmptyCard
          title="Nenhum atendimento encontrado"
          description="Ajuste a busca ou os filtros para localizar os atendimentos desejados."
        />
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
        onPageChange={(page) => {setPage(page)}}
      />
    </>
  );
}
