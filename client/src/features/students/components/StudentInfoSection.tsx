import { Button } from "@/components/ui/button";
import { Collapse } from "@/components/ui/collapse";
import { SummaryItem } from "@/components/ui/summary-item";
import { AddressDetails } from "@/features/address/components/AddressDetails";
import {
  useGetParentById,
  useGetStudentById,
} from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { Edit, Handshake, User } from "lucide-react";
import { ArchiveStudentButton } from "./ArchiveStudentButton";
import { DeleteStudentButton } from "./DeleteStudentButton";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";

interface StudentInfoSectionProps {
  studentId: string;
  onEdit: () => void;
}

export const StudentInfoSection = ({studentId,onEdit}: StudentInfoSectionProps) => {
  const studentQuery = useGetStudentById(studentId);
  const parentQuery = useGetParentById(studentQuery.data?.parentId || "");

  if (studentQuery.error || parentQuery.error) {
    return <ErrorCard title="Erro ao carregar detalhes do aluno" error={studentQuery.error || parentQuery.error} />;
  }

  if (studentQuery.isPending || !studentQuery.data || parentQuery.isPending || !parentQuery.data) {
    return <LoadingCard title="Carregando detalhes do aluno" />;
  }

  return (
    <section className={`card rounded-xl border border-base-300 bg-base-100 shadow-sm`}>
      <div className={`card-body`}>
        <div className="card-actions justify-between">
          <div className="card-title font-bold text-base-content">
            {`${studentQuery.data.name}`}
              <span className={`text-xs md:text-md badge ${studentQuery.data.archivedAt ? "badge-ghost" : "badge-success"} badge-md gap-1`}>
                {studentQuery.data.archivedAt ? "Arquivado" : "Ativo"}
              </span>
              <span className="text-xs md:text-md badge badge-outline badge-md">{studentQuery.data.age} anos</span>
              <span className="text-xs md:text-md badge badge-outline badge-md">{studentQuery.data.school}</span>
          </div>

          <div className="flex gap-2 items-center justify-start">
            <Button onClick={onEdit} variant="primary" size="sm" className="sm:btn-md">
              <Edit className="h-4 w-4 mr-1 sm:mr-2" />
              Editar
            </Button>
            <ArchiveStudentButton className="btn-sm md:btn-md" studentId={studentQuery.data.id} isArchived={!!studentQuery.data.archivedAt} />
            <DeleteStudentButton className="btn-sm md:btn-md" studentId={studentQuery.data.id} />
          </div>
        </div>

        <div className="flex flex-col gap-8">
          <div className="grid gap-8 md:grid-cols-2">
            {/* Dados do Aluno */}
            <div className="animate-[fade-up_500ms_ease-out_both]">
              <h4 className="flex items-center gap-2 text-sm font-bold uppercase tracking-wider text-base-content/50 mb-3 px-1">
                <User size={16} /> Dados do Aluno
              </h4>
              <div className="grid gap-4 border border-base-300 sm:grid-cols-2 bg-base-200/30 p-4 rounded-xl">
                <SummaryItem className="col-span-2" label="E-mail" value={studentQuery.data.email} />
                <SummaryItem label="CPF" value={formatCpf(studentQuery.data.cpf)}/>
                <SummaryItem label="Nascimento" value={formatDateShortYear(studentQuery.data.birthdate)} />
                <SummaryItem label="Contato" value={formatPhone(studentQuery.data.contact)} />
                <SummaryItem label="Data Matrícula" value={formatDateShortYear(studentQuery.data.createdAt)} />
              </div>
            </div>

            {/* Dados do Responsável */}
            <div className="animate-[fade-up_600ms_ease-out_both]">
              <h4 className="flex items-center gap-2 text-sm font-bold uppercase tracking-wider text-base-content/50 mb-3 px-1">
                <Handshake size={16} /> Responsável
              </h4>
              <div className="grid gap-4 border border-base-300 sm:grid-cols-2 bg-base-200/30 p-4 rounded-xl">
                <SummaryItem className="col-span-2" label="Nome Completo" value={parentQuery.data?.name} />
                <SummaryItem label="CPF" value={formatCpf(parentQuery.data?.cpf)} />
                <SummaryItem label="Contato" value={formatPhone(parentQuery.data?.contact)} />
                <SummaryItem className="col-span-2" label="E-mail" value={parentQuery.data?.email} />
              </div>
            </div>
          </div>

          <Collapse
            title={"Endereço"}
            className="mt-2 shadow-sm border border-base-300 hover:bg-base-200/50 transition-colors animate-[fade-up_700ms_ease-out_both]"
          >
            <AddressDetails address={studentQuery.data?.address} />
          </Collapse>
        </div>
      </div>
    </section>
  );
};
