import styles from "@/components/ui/form-field.module.css"
import { cn } from "@/lib/utils"
import type { ReactNode } from "react"

type FormFieldProps = {
  label: string
  htmlFor: string
  error?: string
  help?: string
  className?: string
  children: ReactNode
}

export function FormField({ label, htmlFor, error, help, className, children }: FormFieldProps) {
  return (
    <div className={cn(styles.field, className)}>
      <label className={styles.label} htmlFor={htmlFor}>
        {label}
      </label>
      {children}
      {help ? <p className={styles.help}>{help}</p> : null}
      {error ? <p className={styles.error}>{error}</p> : null}
    </div>
  )
}
