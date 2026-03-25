import type { ReactNode } from "react";
import { UserCog } from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import { ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { PageHeader } from "@/components/ui/page-header";
import { PageLoading } from "@/components/ui/page-loading";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import styles from "./ParentDetailPage.module.css";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

import { StudentsTable } from "@/features/students/components/StudentsTable";
import { EditParentButton } from "./components/EditParentButton";
import { ArchiveParentButton } from "./components/ArchiveParentButton";
import { DeleteParentButton } from "./components/DeleteParentButton";
import { useGetParentById } from "@/kubb";
export function ParentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const parentId = id ?? "";
  const navigate = useNavigate();

  const {
    data: parentData,
    error: parentError,
    isLoading: isParentLoading,
    isFetched: isParentFetched,
  } = useGetParentById(parentId);

  if (isParentLoading) {
    return <PageLoading message="Carregando responsável..." />;
  }

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: parentData?.name },
    { label: "E-mail", value: parentData?.email },
    { label: "Contato", value: parentData?.contact },
    { label: "CPF", value: parentData?.cpf },
    { label: "Status", value: parentData?.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Criado em", value: parentData?.createdAt },
  ];

  if (parentError || !parentData) {
    return (
      <div className={styles.page}>
        <ErrorCard
          description={getFriendlyErrorMessage(parentError)}
          actionLabel="Voltar para listagem de responsáveis"
          onAction={() => navigate("/parents")}
        />
      </div>
    );
  }

  return (
    <div className={styles.page}>
      <PageHeader
        description="Veja e gerencie as informações do responsável"
        Icon={UserCog}
        title="Detalhes do responsável"
        action={
          <ButtonLink to="/parents" variant="outline">
            Voltar para responsáveis
          </ButtonLink>
        }
      />

      {/* RESUMO DO RESPONSÁVEL */}
      <SectionCard
        title="Resumo do responsável"
        description="Dados completos de cadastro, contato e status."
        headerAction={
          <>
            <EditParentButton parentId={parentId} />
            <ArchiveParentButton
              parentId={parentId}
              isArchived={!!parentData?.archivedAt}
            />
            <DeleteParentButton parentId={parentId} />
          </>
        }
      >
        {isParentLoading && <PageLoading message="Carregando responsável..." />}

        {parentError && (
          <div className={styles.page}>
            <ErrorCard
              description={getFriendlyErrorMessage(parentError)}
              actionLabel="Voltar para listagem de responsáveis"
              onAction={() => navigate("/parents")}
            />
          </div>
        )}

        {isParentFetched && (
          <div className={styles.summaryGrid}>
            {summaryItems.map((item) => (
              <SummaryItem
                key={item.label}
                label={item.label}
                value={item.value}
              />
            ))}
          </div>
        )}
      </SectionCard>

      {/* ALUNOS VINCULADOS */}
      <SectionCard
        title="Alunos vinculados"
        description={`Alunos registrados sob a responsabilidade de ${parentData.name}.`}
      >
        <StudentsTable variant="embedded" ownerId={parentId} />
      </SectionCard>
    </div>
  );
}
