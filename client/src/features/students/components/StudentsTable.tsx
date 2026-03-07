import { Button } from "@/components/ui/button"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import styles from "@/features/students/components/StudentsTable.module.css"
import type { StudentResponse } from "@/lib/schemas"
import { Link } from "react-router-dom"

type StudentsTableProps = {
  students: StudentResponse[]
  deletingId: string | null
  onToggleArchive: (student: StudentResponse) => void
}

export function StudentsTable({ students, deletingId, onToggleArchive }: StudentsTableProps) {
  return (
    <div className={styles.tableWrap}>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Nome</TableHead>
            <TableHead>Email</TableHead>
            <TableHead>Idade</TableHead>
            <TableHead>Escola</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Ações</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {students.map((student) => (
            <TableRow key={student.id}>
              <TableCell>{student.name}</TableCell>
              <TableCell>{student.email}</TableCell>
              <TableCell>{student.age}</TableCell>
              <TableCell>{student.school}</TableCell>
              <TableCell>{student.archivedAt ? "Arquivado" : "Ativo"}</TableCell>
              <TableCell>
                <div className={styles.actions}>
                  <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/students/${student.id}`}>
                    Detalhes
                  </Link>
                  <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/students/${student.id}/edit`}>
                    Editar
                  </Link>
                  <Button
                    type="button"
                    variant={student.archivedAt ? "outline" : "destructive"}
                    size="sm"
                    onClick={() => onToggleArchive(student)}
                    disabled={deletingId === student.id}
                  >
                    {deletingId === student.id ? "Salvando..." : student.archivedAt ? "Reativar" : "Arquivar"}
                  </Button>
                </div>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
