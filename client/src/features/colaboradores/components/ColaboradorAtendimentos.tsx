"use client";

import { Banknote } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Card, CardActions, CardHeader, CardTitle } from "@/components/ui/Card";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { Modal } from "@/components/ui/Modal";
import { Pagination } from "@/components/ui/Pagination";
import { ColaboradorAtendimentoCard } from "@/features/colaboradores/components/ColaboradorAtendimentoCard";
import {
  ColaboradorAtendimentosFilters,
  type ColaboradorAtendimentoTipo,
} from "@/features/colaboradores/components/ColaboradorAtendimentosFilters";
import { ColaboradorAtendimentosTable } from "@/features/colaboradores/components/ColaboradorAtendimentosTable";
import { RegistrarRepasseForm } from "@/features/colaboradores/components/repasses/RegistrarRepasseForm";
import { useBuscarAtendimentosIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentosIndividuais";
import { useDebounce } from "@/lib/hooks/use-debounce";

const PAGE_SIZE = 10;

type ColaboradorAtendimentosProps = {
  colaboradorId: string;
};

export function ColaboradorAtendimentos({ colaboradorId }: Readonly<ColaboradorAtendimentosProps>) {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [tipo, setTipo] = useState<ColaboradorAtendimentoTipo>("");
  const [statusCobranca, setStatusCobranca] = useState("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const [isSelectingRepasses, setIsSelectingRepasses] = useState(false);
  const [selectedRepasseIds, setSelectedRepasseIds] = useState<number[]>([]);
  const [isPaymentModalOpen, setIsPaymentModalOpen] = useState(false);
  const search = useDebounce(searchInput.trim(), 300);

  const atendimentos = useBuscarAtendimentosIndividuais({
    page,
    size: PAGE_SIZE,
    colaboradorId,
    busca: search || undefined,
    tipo: tipo || undefined,
    statusCobranca: statusCobranca || undefined,
    inicio: dataInicio ? `${dataInicio}T00:00:00` : undefined,
    fim: dataFim ? `${dataFim}T23:59:59` : undefined,
  });

  const content = atendimentos.data?.content ?? [];
  const metadata = atendimentos.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;
  const selectedRepasseTotal = content.reduce((total, atendimento) => {
    const repasseId = atendimento.repasse.id;

    return repasseId !== undefined && selectedRepasseIds.includes(repasseId)
      ? total + (atendimento.repasse.valor ?? 0)
      : total;
  }, 0);

  function clearSelection() {
    setSelectedRepasseIds([]);
  }

  function resetPageAndSelection() {
    setPage(0);
    clearSelection();
  }

  function toggleRepasse(repasseId: number) {
    setSelectedRepasseIds((current) =>
      current.includes(repasseId) ? current.filter((id) => id !== repasseId) : [...current, repasseId],
    );
  }

  function togglePage(repasseIds: number[]) {
    setSelectedRepasseIds((current) => {
      const allSelected = repasseIds.length > 0 && repasseIds.every((id) => current.includes(id));

      return allSelected
        ? current.filter((id) => !repasseIds.includes(id))
        : Array.from(new Set([...current, ...repasseIds]));
    });
  }

  function cancelSelection() {
    clearSelection();
    setIsSelectingRepasses(false);
    setIsPaymentModalOpen(false);
  }

  function handlePaymentSuccess() {
    setIsPaymentModalOpen(false);
    setIsSelectingRepasses(false);
    clearSelection();
  }

  function changePage(nextPage: number) {
    clearSelection();
    setPage(nextPage);
  }

  return (
    <>
      <Modal
        isOpen={isPaymentModalOpen}
        onClose={() => setIsPaymentModalOpen(false)}
        title="Registrar repasse"
        description="Informe a forma de pagamento dos repasses selecionados."
        size="md"
      >
        <RegistrarRepasseForm
          repasseIds={selectedRepasseIds}
          selectedTotal={selectedRepasseTotal}
          onCancel={() => setIsPaymentModalOpen(false)}
          onSuccess={handlePaymentSuccess}
        />
      </Modal>

      <Card>
        <CardHeader>
          <div>
            <CardTitle>Atendimentos do colaborador</CardTitle>
            <p className="mt-2 text-sm text-base-content/65">
              Consulte os atendimentos vinculados a este colaborador.
            </p>
          </div>

          <CardActions>
            {isSelectingRepasses ? (
              <>
                <span className="self-center text-sm text-base-content/70">
                  {selectedRepasseIds.length} selecionado(s)
                </span>
                <Button type="button" variant="ghost" size="sm" onClick={cancelSelection}>
                  Cancelar
                </Button>
                <Button
                  type="button"
                  size="sm"
                  disabled={selectedRepasseIds.length === 0}
                  onClick={() => setIsPaymentModalOpen(true)}
                >
                  Continuar
                </Button>
              </>
            ) : (
              <Button
                type="button"
                variant="success"
                size="sm"
                onClick={() => {
                  clearSelection();
                  setIsSelectingRepasses(true);
                }}
              >
                <Banknote size={16} />
                Registrar repasse
              </Button>
            )}
          </CardActions>
        </CardHeader>

        <ColaboradorAtendimentosFilters
          search={searchInput}
          tipo={tipo}
          statusCobranca={statusCobranca}
          dataInicio={dataInicio}
          dataFim={dataFim}
          onSearchChange={(value) => {
            resetPageAndSelection();
            setSearchInput(value);
          }}
          onTipoChange={(value) => {
            resetPageAndSelection();
            setTipo(value);
          }}
          onStatusCobrancaChange={(value) => {
            resetPageAndSelection();
            setStatusCobranca(value);
          }}
          onDataInicioChange={(value) => {
            resetPageAndSelection();
            setDataInicio(value);
          }}
          onDataFimChange={(value) => {
            resetPageAndSelection();
            setDataFim(value);
          }}
        />

        {atendimentos.isLoading ? (
          <LoadingSkeleton className="h-64 w-full" />
        ) : atendimentos.error ? (
          <ErrorCard
            title="Não foi possível carregar os atendimentos do colaborador"
            description="A consulta paginada falhou. Tente novamente."
            error={atendimentos.error}
          />
        ) : content.length === 0 ? (
          <EmptyCard title="Nenhum atendimento encontrado" description="Não existem atendimentos para os filtros informados." />
        ) : (
          <div className="space-y-4">
            <ColaboradorAtendimentosTable
              atendimentos={content}
              selectionMode={isSelectingRepasses}
              selectedIds={selectedRepasseIds}
              onToggle={toggleRepasse}
              onTogglePage={togglePage}
              onOpen={(atendimentoId) => router.push(`/atendimentos/${atendimentoId}`)}
            />

            <div className="grid gap-4 md:hidden">
              {content.map((atendimento) => {
                const repasseId = atendimento.repasse.id;
                const selected = repasseId !== undefined && selectedRepasseIds.includes(repasseId);

                return (
                  <ColaboradorAtendimentoCard
                    key={atendimento.id}
                    atendimento={atendimento}
                    selectionMode={isSelectingRepasses}
                    selected={selected}
                    onOpen={() => router.push(`/atendimentos/${atendimento.id}`)}
                    onToggle={() => {
                      if (repasseId !== undefined) {
                        toggleRepasse(repasseId);
                      }
                    }}
                  />
                );
              })}
            </div>

            <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
              <p className="text-sm text-base-content/70">
                Mostrando {content.length} de {totalElements} atendimento(s)
              </p>
              <Pagination
                currentPage={currentPage}
                totalPages={totalPages}
                onPrevious={() => changePage(currentPage - 1)}
                onNext={() => changePage(currentPage + 1)}
              />
            </div>
          </div>
        )}
      </Card>
    </>
  );
}
