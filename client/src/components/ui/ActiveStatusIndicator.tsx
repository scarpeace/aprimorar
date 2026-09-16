import { Tooltip } from "@/components/ui/Tooltip";

type ActiveStatusIndicatorProps = {
  active: boolean;
};

export function ActiveStatusIndicator({ active }: Readonly<ActiveStatusIndicatorProps>) {
  const label = active ? "Ativo" : "Inativo";
  const animationClass = active ? "animate-bounce" : "";
  const colorClass = active ? "status-success" : "status-error";

  return (
    <Tooltip content={label}>
      <span className={`status ${colorClass} ${animationClass}`.trim()} aria-label={label} role="status" />
    </Tooltip>
  );
}
