import { ButtonLink } from "@/components/ui/button"
import type { StudentResponse } from "@/lib/schemas/student"

export type StudentsTableVariant = "studentsPage" | "parentPage"

type StudentsTableProps = {
  students: StudentResponse[]
  variant?: StudentsTableVariant
}

export function StudentsTable({ students, variant = "studentsPage" }: Readonly<StudentsTableProps>) {
  return (
    <div className="app-table-wrap">
      <table className="table table-zebra w-full">
        <thead className="bg-base-200/90">
          <tr>
            <th className="app-th">Nome</th>
            <th className="app-th">Escola</th>
            <th className="app-th-center">Idade</th>
            <th className="app-th">Contato</th>
            {variant === "studentsPage" && <th className="app-th">Responsável</th>}
            <th className="app-th">Ações</th>
          </tr>
        </thead>
        <tbody>
          {students.map((student) => (
            <tr className="transition-colors hover:bg-base-200/70" key={student.id}>
              <td className="font-medium">{student.name}</td>
              <td>{student.school}</td>
              <td className="text-center">{student.age}</td>
              <td>{student.contact}</td>
              {variant === "studentsPage" && <td>{student.parent.name}</td>}
              <td>
                <div className="flex gap-2">
                  <ButtonLink size="sm" to={`/students/${student.id}`} variant="outline">
                    Detalhes
                  </ButtonLink>
                </div>
              </td>
            </tr>
          ))}
          {students.length === 0 && (
            <tr>
              <td colSpan={variant === "studentsPage" ? 6 : 5} className="text-center py-8 text-base-content/50">
                Nenhum aluno encontrado.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}
