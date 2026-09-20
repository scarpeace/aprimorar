"use client";

import { useState } from "react";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { Modal } from "@/components/ui/Modal";
import { Pagination } from "@/components/ui/Pagination";
import { RegistrarPagamentoAlunoForm } from "@/features/cobrancas/components/RegistrarPagamentoAlunoForm";
import { CobrancasPendentesTable } from "@/features/cobrancas/components/CobrancasPendentesTable";
import { useBuscarCobrancasAlunos } from "@/lib/api/generated/hooks/cobranças de alunos/useBuscarCobrancasAlunos";

const PAGE_SIZE = 10;

type RegistrarPagamentoAlunoModalProps = {
  alunoId: string;
  isOpen: boolean;
  onClose: () => void;
};

export function RegistrarPagamentoAlunoModal({ alunoId, isOpen, onClose }: Readonly<RegistrarPagamentoAlunoModalProps>) {
  const [page, setPage] = useState(0);
  const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const cobrancas = useBuscarCobrancasAlunos(
    {
      alunoId,
      status: "PENDENTE",
      page,
      size: PAGE_SIZE,
    },
    {
      query: {
        enabled: isOpen,
      },
    },
  );

  const content = cobrancas.data?.content ?? [];
  const metadata = cobrancas.data?.page;
  const currentPage = metadata?.number ?? page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const selectedTotal = content.reduce(
    (total, cobranca) => selectedIds.includes(cobranca.id) ? total + cobranca.valor : total,
    0,
  );

  function toggleCobranca(cobrancaId: number) {
    setSelectedIds((current) =>
      current.includes(cobrancaId) ? current.filter((id) => id !== cobrancaId) : [...current, cobrancaId],
    );
  }

  function changePage(nextPage: number) {
    setSelectedIds([]);
    setPage(nextPage);
  }

  function handleClose() {
    setPage(0);
    setSelectedIds([]);
    onClose();
  }

  return (
    <Modal
      isOpen={isOpen}
      onClose={handleClose}
      title="Registrar pagamento"
      description="Selecione as cobranças pendentes e informe a forma de pagamento."
      size="lg"
    >
      <div className="space-y-6">
        {cobrancas.isLoading ? (
          <LoadingSkeleton className="h-56 w-full" />
        ) : cobrancas.error ? (
          <ErrorCard
            title="Não foi possível carregar as cobranças pendentes"
            description="A consulta falhou. Tente novamente."
            error={cobrancas.error}
          />
        ) : content.length === 0 ? (
          <EmptyCard
            title="Nenhuma cobrança pendente"
            description="Não existem cobranças pendentes para este aluno."
          />
        ) : (
          <>
            <CobrancasPendentesTable
              cobrancas={content}
              selectedIds={selectedIds}
              onToggle={toggleCobranca}
            />

            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              summary={<>Mostrando {content.length} de {totalElements} cobrança(s) pendente(s)</>}
              onPrevious={() => changePage(currentPage - 1)}
              onNext={() => changePage(currentPage + 1)}
            />

            <RegistrarPagamentoAlunoForm
              cobrancaIds={selectedIds}
              selectedTotal={selectedTotal}
              onCancel={handleClose}
              onSuccess={handleClose}
            />
          </>
        )}
      </div>
    </Modal>
  );
}
