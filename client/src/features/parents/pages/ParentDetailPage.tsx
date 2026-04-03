import { ErrorCard } from "@/components/ui/error-card";
import { PageHeader } from "@/components/ui/page-header";
import { Handshake } from "lucide-react";
import { useParams } from "react-router-dom";

import { LoadingCard } from "@/components/ui/loading-card";
import { useGetStudentsByParent } from "@/kubb";
import { useState } from "react";
import { StudentsTable } from "../../students/components/StudentsTable";
import { ParentDetails } from "../components/ParentDetails";
import { useParentById } from "../hooks/use-parent-queries";

export function ParentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const parentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: parent,
    isError: isParentError,
    isPending: isParentPending,
    error: parentError,
  } = useParentById({ parentId });

  const {
    data: parentStudents,
    isPending: isParentStudentsPending,
    error: parentStudentsError,
  } = useGetStudentsByParent({parentId} );

  if (isParentError) {
    return (
      <ErrorCard title="Erro ao carregar responsável" error={parentError} />
    );
  }

  if (isParentPending) {
    return <LoadingCard title="Carregando dados do responsável" />;
  }

  return (
    <>
      <PageHeader
        description="Veja e gerencie as informações do responsável"
        title="Detalhes do Responsável"
        Icon={Handshake}
        backLink={"/parents"}
      />

      <div className="flex flex-col gap-7">
        <ParentDetails parent={parent} />
        <StudentsTable
          students={parentStudents}
          onPageChange={setCurrentPage}
          currentPage={currentPage}
          isPending={isParentStudentsPending}
          error={parentStudentsError}
        />
      </div>
    </>
  );
}
