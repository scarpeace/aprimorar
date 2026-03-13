import { cn } from "@/lib/utils"

type SummaryFieldProps = {
  label: string
  value: string
  className?: string
  labelClassName?: string
  valueClassName?: string
}

export function SummaryField({
  label,
  value,
  className,
  labelClassName,
  valueClassName,
}: SummaryFieldProps) {
  return (
    <div className={className}>
      <p className={cn(labelClassName)}>{label}</p>
      <p className={cn(valueClassName)}>{value}</p>
    </div>
  )
}
