"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { Pagination } from "@/components/ui/Pagination";
import {
  AlunoAtendimentosFilters,
  type AlunoAtendimentoTipo,
} from "@/features/alunos/components/atendimentos/AlunoAtendimentosFilters";
import { AlunoAtendimentosTable } from "@/features/alunos/components/atendimentos/AlunoAtendimentosTable";
import { useBuscarAtendimentosIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentosIndividuais";
import { useDebounce } from "@/lib/hooks/use-debounce";

const PAGE_SIZE = 10;

type AlunoAtendimentosProps = {
  alunoId: string;
};

export function AlunoAtendimentos({ alunoId }: Readonly<AlunoAtendimentosProps>) {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [tipo, setTipo] = useState<AlunoAtendimentoTipo>("");
  const [statusCobranca, setStatusCobranca] = useState("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const search = useDebounce(searchInput.trim(), 300);

  const atendimentos = useBuscarAtendimentosIndividuais({
    page,
    size: PAGE_SIZE,
    alunoId,
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

  function resetPage() {
    setPage(0);
  }

  function handleSearchChange(value: string) {
    resetPage();
    setSearchInput(value);
  }

  function handleTipoChange(value: AlunoAtendimentoTipo) {
    resetPage();
    setTipo(value);
  }

  function handleStatusCobrancaChange(value: string) {
    resetPage();
    setStatusCobranca(value);
  }

  function handleDataInicioChange(value: string) {
    resetPage();
    setDataInicio(value);
  }

  function handleDataFimChange(value: string) {
    resetPage();
    setDataFim(value);
  }

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Atendimentos do aluno</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">Consulte os atendimentos vinculados a este aluno.</p>
        </div>
      </CardHeader>

      <AlunoAtendimentosFilters
        search={searchInput}
        tipo={tipo}
        statusCobranca={statusCobranca}
        dataInicio={dataInicio}
        dataFim={dataFim}
        onSearchChange={handleSearchChange}
        onTipoChange={handleTipoChange}
        onStatusCobrancaChange={handleStatusCobrancaChange}
        onDataInicioChange={handleDataInicioChange}
        onDataFimChange={handleDataFimChange}
      />

      {atendimentos.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : atendimentos.error ? (
        <ErrorCard
          title="Não foi possível carregar os atendimentos do aluno"
          description="A consulta paginada falhou. Tente novamente."
          error={atendimentos.error}
        />
      ) : content.length === 0 ? (
        <EmptyCard title="Nenhum atendimento encontrado" description="Não existem atendimentos vinculados a este aluno." />
      ) : (
        <div className="space-y-4">
          <AlunoAtendimentosTable
            atendimentos={content}
            onOpen={(atendimentoId) => router.push(`/atendimentos/${atendimentoId}`)}
          />

          <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
            <p className="text-sm text-base-content/70">
              Mostrando {content.length} de {totalElements} atendimento(s)
            </p>

            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              onPrevious={() => setPage((value) => value - 1)}
              onNext={() => setPage((value) => value + 1)}
            />
          </div>
        </div>
      )}
    </Card>
  );
}
