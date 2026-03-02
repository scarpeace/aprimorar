import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import styles from "@/features/events/EventCreatePage.module.css"

export function EventCreatePage() {
  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Novo evento</h1>
          <p className="text-sm text-gray-600">Crie um novo atendimento/aula.</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/events">← Voltar para eventos</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Em breve</CardTitle>
          <CardDescription>Esta pagina e um placeholder para o formulario de criacao de evento.</CardDescription>
        </CardHeader>
        <CardContent>
          <Button asChild type="button">
            <Link to="/events">Ver eventos</Link>
          </Button>
        </CardContent>
      </Card>
    </div>
  )
}
