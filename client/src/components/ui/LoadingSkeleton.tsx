type LoadingSkeletonProps = {
  className?: string;
};

export function LoadingSkeleton({ className = "h-32 w-full" }: Readonly<LoadingSkeletonProps>) {
  return <div aria-hidden="true" className={`skeleton ${className}`} />;
}
