import { Search } from "lucide-react"

type ListSearchInputProps = {
  placeholder: string
  ariaLabel?: string
  className?: string
}

export function ListSearchInput({ placeholder, ariaLabel, className }: ListSearchInputProps) {
  return (
    <label className={`input input-bordered flex w-full max-w-xl items-center gap-2 ${className ?? ""}`.trim()}>
      <Search className="h-4 w-4 text-base-content/60" aria-hidden="true" />
      <input
        type="search"
        className="grow"
        placeholder={placeholder}
        aria-label={ariaLabel ?? placeholder}
      />
    </label>
  )
}
