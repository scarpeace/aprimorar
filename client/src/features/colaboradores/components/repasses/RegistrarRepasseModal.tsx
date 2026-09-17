"use client";

import { useState } from "react";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { Modal } from "@/components/ui/Modal";
import { Pagination } from "@/components/ui/Pagination";
import { RegistrarRepasseForm } from "@/features/colaboradores/components/repasses/RegistrarRepasseForm";
import { RegistrarRepasseTable } from "@/features/colaboradores/components/repasses/RegistrarRepasseTable";
import { useBuscarRepassesIndividuais } from "@/lib/api/generated/hooks/repasses individuais/useBuscarRepassesIndividuais";

const PAGE_SIZE = 10;

type RegistrarRepasseModalProps = {
  colaboradorId: string;
  isOpen: boolean;
  onClose: () => void;
};

export function RegistrarRepasseModal({ colaboradorId, isOpen, onClose }: Readonly<RegistrarRepasseModalProps>) {
  const [page, setPage] = useState(0);
  const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const repasses = useBuscarRepassesIndividuais(
    {
      colaboradorId,
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

  const content = repasses.data?.content ?? [];
  const metadata = repasses.data?.page;
  const currentPage = metadata?.number ?? page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const selectedTotal = content.reduce(
    (total, repasse) => selectedIds.includes(repasse.id) ? total + repasse.valor : total,
    0,
  );

  function toggleRepasse(repasseId: number) {
    setSelectedIds((current) =>
      current.includes(repasseId) ? current.filter((id) => id !== repasseId) : [...current, repasseId],
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
      title="Registrar repasse"
      description="Selecione os repasses pendentes e informe a forma de pagamento."
      size="lg"
    >
      <div className="space-y-6">
        {repasses.isLoading ? (
          <LoadingSkeleton className="h-56 w-full" />
        ) : repasses.error ? (
          <ErrorCard
            title="Não foi possível carregar os repasses pendentes"
            description="A consulta falhou. Tente novamente."
            error={repasses.error}
          />
        ) : content.length === 0 ? (
          <EmptyCard
            title="Nenhum repasse pendente"
            description="Não existem repasses pendentes para este colaborador."
          />
        ) : (
          <>
            <RegistrarRepasseTable repasses={content} selectedIds={selectedIds} onToggle={toggleRepasse} />

            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              summary={<>Mostrando {content.length} de {totalElements} repasse(s) pendente(s)</>}
              onPrevious={() => changePage(currentPage - 1)}
              onNext={() => changePage(currentPage + 1)}
            />

            <RegistrarRepasseForm
              repasseIds={selectedIds}
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
