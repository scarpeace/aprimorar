type UsuarioStatusBadgeProps = {
  enabled?: boolean;
};

export function UsuarioStatusBadge({ enabled }: Readonly<UsuarioStatusBadgeProps>) {
  return (
    <span className={`badge badge-sm ${enabled ? "badge-success" : "badge-ghost"}`}>
      {enabled ? "Ativo" : "Inativo"}
    </span>
  );
}
