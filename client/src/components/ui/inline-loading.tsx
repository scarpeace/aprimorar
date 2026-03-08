import { Loader2 } from "lucide-react"

type InlineLoadingProps = {
  message?: string
}

export function InlineLoading({ message = "Carregando..." }: InlineLoadingProps) {
  return (
    <div className="flex items-center gap-2 text-sm text-muted-foreground">
      <Loader2 className="h-4 w-4 animate-spin" />
      <span>{message}</span>
    </div>
  )
}
