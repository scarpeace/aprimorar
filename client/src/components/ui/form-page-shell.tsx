import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import styles from "@/components/ui/form-page-shell.module.css"
import type { ReactNode } from "react"
import { Link } from "react-router-dom"

type FormPageShellProps = {
  title: string
  description: string
  backTo: string
  backLabel: string
  cardTitle: string
  cardDescription: string
  children: ReactNode
}

export function FormPageShell({
  title,
  description,
  backTo,
  backLabel,
  cardTitle,
  cardDescription,
  children,
}: FormPageShellProps) {
  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">{title}</h1>
          <p className="text-sm text-gray-600">{description}</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to={backTo}>{backLabel}</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>{cardTitle}</CardTitle>
          <CardDescription>{cardDescription}</CardDescription>
        </CardHeader>
        <CardContent>{children}</CardContent>
      </Card>
    </div>
  )
}
