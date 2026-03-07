import { Button } from "@/components/ui/button"
import styles from "@/components/ui/list-page-header.module.css"
import { Link } from "react-router-dom"

type ListPageHeaderProps = {
  title: string
  description: string
  actionLabel?: string
  actionTo?: string
}

export function ListPageHeader({ title, description, actionLabel, actionTo }: ListPageHeaderProps) {
  return (
    <div className={styles.header}>
      <div>
        <h1 className="text-3xl font-bold text-gray-900">{title}</h1>
        <p className="text-sm text-gray-600">{description}</p>
      </div>
      {actionLabel && actionTo ? (
        <Button asChild type="button">
          <Link to={actionTo}>{actionLabel}</Link>
        </Button>
      ) : null}
    </div>
  )
}
