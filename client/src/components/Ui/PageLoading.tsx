
type PageLoadingProps = {
  message?: string;
};

export function PageLoading({ message = "Carregando..." }: PageLoadingProps) {

  return (
    <div className="flex h-100 items-center justify-center gap-2 text-sm text-base-content/70">
      <div className="flex flex-col gap-12">
        <div className="flex gap-2 items-center">
          <span className="text-center">{message}</span>
          <span className="loading loading-dots loading-xs"></span>
        </div>
      </div>
    </div>
  );
}
