import styles from "@/components/ui/action-error-banner.module.css"

type ActionErrorBannerProps = {
  message: string | null
}

export function ActionErrorBanner({ message }: ActionErrorBannerProps) {
  if (!message) {
    return null
  }

  return <div className={styles.banner}>{message}</div>
}
