import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"

export function EmployeeCreatePage() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Novo colaborador</h1>
          <p className="text-sm text-gray-600">Crie um novo cadastro de colaborador.</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/employees">← Voltar para colaboradores</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Em breve</CardTitle>
          <CardDescription>Esta pagina e um placeholder para o formulario de criacao de colaborador.</CardDescription>
        </CardHeader>
        <CardContent>
          <Button asChild type="button">
            <Link to="/employees">Ver colaboradores</Link>
          </Button>
        </CardContent>
      </Card>
    </div>
  )
}
