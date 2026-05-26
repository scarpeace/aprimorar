import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetAlunos } from "@/kubb";
import { useDebounce } from "@/lib/hooks/use-debounce";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export function StudentsTable() {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [hideCharged, setHideCharged] = useState(false);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const alunosQuery = useGetAlunos({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  const handleHideChargedChange = (value: boolean) => {
    setHideCharged(value);
    setCurrentPage(0);
  };

  const onPageChange = (page: number) => {
    setCurrentPage(page);
  };

  const handleShowArchivedChange = (value: boolean) => {
    setShowArchived(value);
    setCurrentPage(0);
  };


  if (alunosQuery.isError) {
    return <ErrorCard title="Não foi possível carregar a listagem de Alunos" error={alunosQuery.error}/>;
  }

  if (alunosQuery.isLoading) {
    return <LoadingSpinner text="Carregando Alunos..." />;
  }

  if (!alunosQuery.data || !alunosQuery.data.alunos || alunosQuery.data.alunos.totalElements === 0) {
    return (
      <EmptyCard
        title="Nenhum aluno encontrado"
        description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
      />
    );
  }

  return (
    <>
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">

          <ListSearchInput
            className="grow"
            placeholder="Buscar aluno por nome, email ou CPF"
            ariaLabel="Buscar aluno"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <div className="flex w-full flex-row items-start justify-between gap-3 xl:w-auto xl:justify-end">
            <ToggleSwitch
              label="Arquivados"
              tip="Mostrar alunos arquivados"
              toggled={showArchived}
              setToggle={handleShowArchivedChange}
              className="border-info/25 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
            />
            <ToggleSwitch
              label="Ocultar cobrados"
              tip="Mostrar apenas alunos com pendencias no periodo"
              toggled={hideCharged}
              setToggle={handleHideChargedChange}
              className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
              />
          </div>
        </div>
      </section>

      {/*TABELA*/}
      <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra bg-base-100 animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-200/80">
          <tr>
            <th className="text-left font-semibold text-base-content/80">Nome</th>
            <th className="text-left font-semibold text-base-content/80">CPF</th>
            <th className="text-left font-semibold text-base-content/80">Contato</th>
            <th className="text-left font-semibold text-base-content/80">Escola</th>
            <th className="text-left font-semibold text-base-content/80">Matricula</th>
            <th className="text-left font-semibold text-base-content/80">Status</th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {alunosQuery.data.alunos?.content?.map((student) => {
            return (
              <tr
                key={student.id}
                className={`transition-colors hover:bg-base-200/70 hover:cursor-pointer`}
                onClick={() => student.id && navigate(`/students/${student.id}`)}
              >
                <td className="font-bold">{student.name}</td>
                <td>{formatCpf(student.cpf)}</td>
                <td>{formatPhone(student.contact)}</td>
                <td>{student.school}</td>
                <td>{formatDateShortYear(student.createdAt)}</td>
                 <td>
                   <span className={`badge ${(student.active ?? true) ? "badge-success" : "badge-ghost"} badge-sm`}>
                     {(student.active ?? true) ? "Ativo" : "Arquivado"}
                   </span>
                 </td>
               </tr>
            );
          })}
        </tbody>
      </table>
      </div>
      <Pagination
        totalElements={alunosQuery.data.alunos?.totalElements ?? 0}
        totalPages={alunosQuery.data.alunos?.totalPages ?? 0}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
