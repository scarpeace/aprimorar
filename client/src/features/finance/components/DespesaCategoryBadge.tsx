import { DESPESA_CATEGORY_LABEL } from "../lib/despesa-category-labels";

const DESPESA_CATEGORY_BADGE_COLOR: Record<string, string> = {
  CONTAS: "border-error/20 bg-error/10 text-error",
  ADMINISTRATIVO: "border-info/20 bg-info/10 text-info",
  DESPENSA: "border-success/20 bg-success/10 text-success",
  MANUTENCAO: "border-warning/30 bg-warning/10 text-warning",
  SERVICOS: "border-primary/20 bg-primary/10 text-primary",
  MATERIAIS: "border-secondary/20 bg-secondary/10 text-secondary",
  ASSINATURAS: "badge-ghost border-base-300 text-base-content/70",
};

type DespesaCategoryBadgeProps = {
  category?: string | null;
};

export function DespesaCategoryBadge({
  category,
}: Readonly<DespesaCategoryBadgeProps>) {
  if (!category) {
    return (
      <span className="badge badge-ghost badge-sm border-base-300 px-3 py-3">
        Sem categoria
      </span>
    );
  }

  const label = DESPESA_CATEGORY_LABEL[category] ?? category;
  const color =
    DESPESA_CATEGORY_BADGE_COLOR[category] ??
    "badge-ghost border-base-300 text-base-content/70";

  return (
    <span className={`badge badge-sm border px-3 py-3 ${color}`}>{label}</span>
  );
}
