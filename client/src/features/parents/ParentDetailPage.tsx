import { ErrorCard } from "@/components/ui/error-card";
import { PageHeader } from "@/components/ui/page-header";
import { Handshake } from "lucide-react";
import { useParams } from "react-router-dom";

import { LoadingCard } from "@/components/ui/loading-card";
import { StudentsTable } from "../students/components/StudentsTable";
import { useStudentsByParent } from "../students/hooks/use-students-query";
import { ArchiveParentButton } from "./components/ArchiveParentButton";
import { DeleteParentButton } from "./components/DeleteParentButton";
import { EditParentButton } from "./components/EditParentButton";
import { useParentById } from "./hooks/use-parent-queries";
import { ParentDetails } from "./components/ParentDetails";
import { Alert } from "@/components/ui/alert";

//TODO: O responsável responsável tá podendo ser arquivado/desarquivado, tem que arrumar
export function ParentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const parentId = id ?? "";


  const {
    data: parent,
    isPending: isParentPending,
    isLoading: isParentLoading,
    error: parentError,
    isError: isParentError,
  } = useParentById({ parentId });

  const {
    data: parentStudents,
    isPending: isParentStudentsPending,
    error: parentStudentsError,
    isError: isParentStudentsError
  } = useStudentsByParent({parentId});


  if (isParentError || isParentStudentsError) {
    console.log(isParentError, isParentStudentsError)
    return <ErrorCard title="Erro ao carregar responsável" error={parentError || parentStudentsError} />;
  }

  if (isParentPending || isParentLoading) {
    return <LoadingCard title="Carregando dados do responsável" />;
  }

  return (
    <>
      <PageHeader
        description="Veja e gerencie as informações do responsável"
        title="Detalhes do Responsável"
        Icon={Handshake}
      >
        <div className="flex flex-row ml-auto mt-auto gap-3">
          <EditParentButton parentId={parentId} />
          <ArchiveParentButton
            parentId={parentId}
            isArchived={!!parent.archivedAt}
          />
          <DeleteParentButton parentId={parentId} />
        </div>
      </PageHeader>

      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <ParentDetails parent={parent} />
        <StudentsTable
          students={parentStudents}
          isPending={isParentStudentsPending}
          error={parentStudentsError}
        />
      </div>
    </>
  );
}
