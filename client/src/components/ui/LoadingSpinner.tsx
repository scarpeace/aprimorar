type LoadingSpinnerProps = {
  text?: string;
  className?: string;
};

export function LoadingSpinner({ text, className = "" }: LoadingSpinnerProps) {
  return (
    <div className={`flex items-center gap-3 ${className}`}>
      <span className="loading loading-spinner loading-lg text-primary"></span>
      {text ? <span className="font-medium text-base-content/70">{text}</span> : null}
    </div>
  );
}

