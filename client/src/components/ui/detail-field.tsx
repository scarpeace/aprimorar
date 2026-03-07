import styles from "@/components/ui/detail-field.module.css"
import type { ElementType } from "react"

type DetailFieldProps = {
  label: string
  value: string
  icon?: ElementType
}

export function DetailField({ label, value, icon: Icon }: DetailFieldProps) {
  return (
    <div className={styles.field}>
      {Icon ? <Icon className={styles.icon} /> : null}
      <div className={styles.content}>
        <p className={styles.label}>{label}</p>
        <p className={styles.value}>{value}</p>
      </div>
    </div>
  )
}
