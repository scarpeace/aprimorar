type LoadingSpinnerProps = {
  text?: string;
  className?: string;
};

export function LoadingSpinner({ text, className = "" }: LoadingSpinnerProps) {
  return (
    <div className={`flex items-center gap-3 ${className}`}>
      <span className="loading loading-spinner loading-lg text-primary"></span>
      {text && <span className="text-base-content/70 font-medium">{text}</span>}
    </div>
  );
}
