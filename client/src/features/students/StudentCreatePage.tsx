import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"

export function StudentCreatePage() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Novo aluno</h1>
          <p className="text-sm text-gray-600">Crie um novo cadastro de aluno.</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/students">← Voltar para alunos</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Em breve</CardTitle>
          <CardDescription>Esta pagina e um placeholder para o formulario de criacao de aluno.</CardDescription>
        </CardHeader>
        <CardContent>
          <Button asChild type="button">
            <Link to="/students">Ver alunos</Link>
          </Button>
        </CardContent>
      </Card>
    </div>
  )
}
