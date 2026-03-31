type LoadingSpinnerProps = {
  text: string;
};

export function LoadingSpinner({ text }: LoadingSpinnerProps) {
  return <span className="loading loading-spinner loading-xl">{text}</span>;
}
