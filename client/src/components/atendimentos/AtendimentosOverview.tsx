"use client";

import { useState, type FormEvent } from "react";
import { AtendimentoForm } from "@/components/atendimentos/AtendimentoForm";
import { AtendimentosFilters } from "@/components/atendimentos/AtendimentosFilters";
import { AtendimentosMonthTabs } from "@/components/atendimentos/AtendimentosMonthTabs";
import { AtendimentosResults } from "@/components/atendimentos/AtendimentosResults";
import type {
  AtendimentoResponseStatusEnumKey,
  AtendimentoResponseTipoEnumKey,
} from "@/lib/api/generated/types/AtendimentoResponse";
import { useGetAtendimentos } from "@/lib/api/generated/hooks/atendimento/useGetAtendimentos";
import { Modal } from "@/components/ui/Modal";

const PAGE_SIZE = 20;

export function AtendimentosOverview() {
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [search, setSearch] = useState("");
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const [status, setStatus] = useState<AtendimentoResponseStatusEnumKey | "">("");
  const [tipo, setTipo] = useState<AtendimentoResponseTipoEnumKey | "">("");
  const [selectedYear, setSelectedYear] = useState(() => new Date().getFullYear());
  const [selectedMonthIndex, setSelectedMonthIndex] = useState(() => new Date().getMonth());
  const anoMes = `${selectedYear}-${String(selectedMonthIndex + 1).padStart(2, "0")}`;

  const atendimentos = useGetAtendimentos({
    page,
    size: PAGE_SIZE,
    anoMes,
    busca: search || undefined,
    status: status || undefined,
    tipo: tipo || undefined,
    sort: ["dataHoraInicio,desc", "id,asc"],
  });

  const content = atendimentos.data?.content ?? [];
  const metadata = atendimentos.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;
  const hasPrevious = currentPage > 0;
  const hasNext = totalPages > 0 && currentPage < totalPages - 1;

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setPage(0);
    setSearch(searchInput.trim());
  }

  function handleStatusChange(value: AtendimentoResponseStatusEnumKey | "") {
    setPage(0);
    setStatus(value);
  }

  function handleTipoChange(value: AtendimentoResponseTipoEnumKey | "") {
    setPage(0);
    setTipo(value);
  }

  function handlePreviousYear() {
    setSelectedYear((value) => value - 1);
    setPage(0);
  }

  function handleNextYear() {
    setSelectedYear((value) => value + 1);
    setPage(0);
  }

  function handleMonthChange(monthIndex: number) {
    setSelectedMonthIndex(monthIndex);
    setPage(0);
  }

  const pagination = {
    totalElements,
    currentPage,
    totalPages,
    hasPrevious,
    hasNext,
    onPrevious: () => setPage((value) => value - 1),
    onNext: () => setPage((value) => value + 1),
  };

  return (
    <section className="app-shell-card p-6">
      <AtendimentosFilters
        searchInput={searchInput}
        status={status}
        tipo={tipo}
        onSearchInputChange={setSearchInput}
        onStatusChange={handleStatusChange}
        onTipoChange={handleTipoChange}
        onSubmit={handleSubmit}
        onCreate={() => setIsCreateOpen(true)}
      />

      <AtendimentosMonthTabs
        selectedYear={selectedYear}
        selectedMonthIndex={selectedMonthIndex}
        onPreviousYear={handlePreviousYear}
        onNextYear={handleNextYear}
        onMonthChange={handleMonthChange}
      >
        <AtendimentosResults
          atendimentos={content}
          isLoading={atendimentos.isLoading}
          error={atendimentos.error}
          pagination={pagination}
        />
      </AtendimentosMonthTabs>

      <Modal
        isOpen={isCreateOpen}
        onClose={() => setIsCreateOpen(false)}
        title="Cadastrar atendimento"
        description="Defina participante, horário e valores para criar um novo atendimento."
        size="lg"
      >
        <AtendimentoForm onSuccess={() => setIsCreateOpen(false)} onCancel={() => setIsCreateOpen(false)} />
      </Modal>
    </section>
  );
}
