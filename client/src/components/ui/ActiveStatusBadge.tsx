type ActiveStatusBadgeProps = {
  active: boolean;
};

export function ActiveStatusBadge({ active }: Readonly<ActiveStatusBadgeProps>) {
  return <span className={`badge badge-sm ${active ? "badge-success" : "badge-ghost"}`}>{active ? "Ativo" : "Inativo"}</span>;
}
