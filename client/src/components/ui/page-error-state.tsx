import { EmptyState } from "@/components/ui/empty-state"
import { ListPageHeader } from "@/components/ui/list-page-header"
import styles from "@/components/ui/page-error-state.module.css"

type PageErrorStateProps = {
  title: string
  description: string
  errorMessage: string
  retryLabel?: string
  onRetry: () => void
}

export function PageErrorState({
  title,
  description,
  errorMessage,
  retryLabel = "Tentar novamente",
  onRetry,
}: PageErrorStateProps) {
  return (
    <div className={styles.wrap}>
      <ListPageHeader title={title} description={description} />
      <EmptyState title="Não foi possível carregar" description={errorMessage} actionLabel={retryLabel} onAction={onRetry} />
    </div>
  )
}
