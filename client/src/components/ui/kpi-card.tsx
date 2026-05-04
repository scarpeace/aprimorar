import type { LucideIcon } from "lucide-react"
import type { ReactNode } from "react"

interface KpiCardProps {
  label: string
  value: ReactNode
  className?: string
  Icon: LucideIcon
}

export function KpiCard({ label, value, className, Icon }: KpiCardProps) {
  return (
    <div className={`card border border-base-300 rounded-xl bg-base-100 shadow-sm transition-all hover:shadow-md ${className ?? ""}`}>

      <div className="flex flex-row items-center p-4 sm:p-6 gap-4">
            {/* Left Side: Icon */}
            {Icon && (
              <div className="p-3 rounded-xl bg-primary/10 text-primary">
                <Icon size={24} />
              </div>
            )}
            {/* Right Side: Label and Value stacked */}
            <div className="flex flex-col gap-1">
              <h2 className="text-xs sm:text-sm font-semibold uppercase opacity-60">
                {label}
              </h2>
              <div className="text-xl sm:text-3xl font-bold">
                {value}
              </div>
            </div>
      </div>
    </div>
  )
}
