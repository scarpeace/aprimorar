import styles from "@/components/ui/form-actions.module.css"
import type { ReactNode } from "react"

type FormActionsProps = {
  children: ReactNode
}

export function FormActions({ children }: FormActionsProps) {
  return <div className={styles.actions}>{children}</div>
}
