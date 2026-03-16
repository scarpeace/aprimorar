type PageLoadingProps = {
  message?: string
}

export function PageLoading({ message = "Carregando..." }: PageLoadingProps) {
  return (
    <div className="app-text-muted flex min-h-[200px] items-center justify-center gap-2 text-sm">
      <span className="loading loading-spinner loading-sm text-primary" />
      <span>{message}</span>
    </div>
  )
}
