import {
  useGetAtendimentos,
  type AtendimentoResponseStatusEnumKey,
  type AtendimentoResponseTipoEnumKey,
} from "@/kubb";
import { useNavigate } from "react-router-dom";
import { AtendimentosTableFilters } from "./AtendimentosTable.Filters.tsx";
import { AtendimentosTableListContent } from "./AtendimentosTable.ListContent.tsx";
import { AtendimentosTablePeriodTabs } from "./AtendimentosTable.PeriodTabs.tsx";
import { useDebounce } from "@/hooks/useDebounce.ts";
import { useId, useState } from "react";

type AtendimentosTableProps = {
  openForm: () => void;
};

export function AtendimentosTable({ openForm }: AtendimentosTableProps) {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [filterStatus, setFilterStatus] = useState<AtendimentoResponseStatusEnumKey | "">("");
  const [filterTipo, setFilterTipo] = useState<AtendimentoResponseTipoEnumKey | "">("");
  const now = new Date();
  const [selectedYear, setSelectedYear] = useState(now.getFullYear());
  const [selectedMonthIndex, setSelectedMonthIndex] = useState(now.getMonth());
  const monthTabsGroup = useId();
  const debouncedSearch = useDebounce(search, 500);
  const anoMes = `${selectedYear}-${String(selectedMonthIndex + 1).padStart(2, "0")}`;

  const eventsQuery = useGetAtendimentos({
    page,
    size: 20,
    anoMes,
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
      <AtendimentosTableFilters
        openForm={openForm}
        filterStatus={filterStatus}
        filterTipo={filterTipo}
        onSearchChange={(value) => {
          setSearch(value);
          setPage(0);
        }}
        onStatusChange={(value) => {
          setFilterStatus(value);
          setPage(0);
        }}
        onTipoChange={(value) => {
          setFilterTipo(value);
          setPage(0);
        }}
      />

      <AtendimentosTablePeriodTabs
        selectedYear={selectedYear}
        selectedMonthIndex={selectedMonthIndex}
        monthTabsGroup={monthTabsGroup}
        onPrevYear={() => {
          setSelectedYear((currentYear) => currentYear - 1);
          setPage(0);
        }}
        onNextYear={() => {
          setSelectedYear((currentYear) => currentYear + 1);
          setPage(0);
        }}
        onMonthChange={(monthIndex) => {
          setSelectedMonthIndex(monthIndex);
          setPage(0);
        }}
      >
        <AtendimentosTableListContent
          eventsQuery={eventsQuery}
          events={events}
          hasEvents={hasEvents}
          page={page}
          pagination={pagination}
          onPageChange={setPage}
          onRowClick={(id) => navigate(`/atendimentos/${id}`)}
        />
      </AtendimentosTablePeriodTabs>
    </>
  );
}
