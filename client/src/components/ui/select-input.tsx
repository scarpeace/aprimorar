import styles from "@/components/ui/select-input.module.css"
import { cn } from "@/lib/utils"
import type { SelectHTMLAttributes } from "react"

type SelectInputProps = SelectHTMLAttributes<HTMLSelectElement>

export function SelectInput({ className, ...props }: SelectInputProps) {
  return <select className={cn(styles.select, className)} {...props} />
}
