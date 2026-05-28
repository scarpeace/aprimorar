import { Button } from "@/components/ui/button";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetAlunos } from "@/kubb";
import { useDebounce } from "@/lib/hooks/use-debounce";
import {formatCpf} from "@/lib/utils/formatter";
import { BrushCleaning} from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export function AlunosTable() {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const alunosQuery = useGetAlunos({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  const onPageChange = (page: number) => {
    setCurrentPage(page);
  };

  const handleShowArchivedChange = (value: boolean) => {
    setShowArchived(value);
    setCurrentPage(0);
  };

  const handleCleanFilter = () => {
    setSearchTerm("");
    setShowArchived(false);
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
      <>
      <EmptyCard
        title="Nenhum aluno encontrado"
        description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
        action={<Button variant="outline" onClick={handleCleanFilter}>Limpar Filtros<BrushCleaning size={18}/></Button>}
        />
      </>
    );
  }

  return (
    <main className="">
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">

          <ListSearchInput
            className="grow"
            placeholder="Buscar aluno por nome, email ou CPF"
            ariaLabel="Buscar aluno"
            value={searchTerm}
            onChange={setSearchTerm}
          />
            <ToggleSwitch
              label="Arquivados"
              tip="Mostrar alunos arquivados"
              toggled={showArchived}
              setToggle={handleShowArchivedChange}
              className="border-info/25 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
            />
        </div>
      </section>

      {/*TABELA*/}
      <div className="overflow-x-auto  rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra bg-base-100 animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-200/80">
          <tr>
            <th className="text-left font-semibold text-base-content/80">Nome</th>
            <th className="text-left font-semibold text-base-content/80">CPF</th>
            <th className="text-left font-semibold text-base-content/80">Idade</th>
            {/*<th className="text-left font-semibold text-base-content/80">Contato</th>*/}
            {/*<th className="text-left font-semibold text-base-content/80">Escola</th>*/}
            {/*<th className="text-left font-semibold text-base-content/80">Matricula</th>*/}
            <th className="text-left font-semibold text-base-content/80">Status</th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {alunosQuery.data.alunos?.content?.map((aluno) => {
            return (
              <tr
                key={aluno.id}
                className={`transition-colors hover:bg-base-200/70 hover:cursor-pointer`}
                onClick={() => aluno.id && navigate(`/students/${aluno.id}`)}
              >
                <td className="font-bold">{aluno.name}</td>
                <td>{formatCpf(aluno.cpf)}</td>
                <td>{aluno.age}</td>
                {/*<td>{formatPhone(aluno.contact)}</td>*/}
                {/*<td>{aluno.school}</td>*/}
                {/*<td>{formatDateShortYear(aluno.createdAt)}</td>*/}
                 <td>
                   <span className={`badge ${(aluno.active ?? true) ? "badge-success" : "badge-ghost"} badge-sm`}>
                     {(aluno.active ?? true) ? "Ativo" : "Arquivado"}
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
        size={alunosQuery.data.alunos.size}
      />
    </main>
  );
}
