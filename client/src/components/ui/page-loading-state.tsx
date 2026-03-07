import styles from "@/components/ui/page-loading-state.module.css"

type PageLoadingStateProps = {
  label: string
}

export function PageLoadingState({ label }: PageLoadingStateProps) {
  return <div className={styles.loading}>{label}</div>
}
