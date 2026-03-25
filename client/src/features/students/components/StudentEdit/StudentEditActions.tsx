import { Button, ButtonLink } from "@/components/ui/button";
import { DeleteStudentButton } from "../DeleteStudentButton";

type StudentEditActionsProps = {
  studentId: string;
  isSubmitting: boolean;
};

export function StudentEditActions({
  studentId,
  isSubmitting,
}: Readonly<StudentEditActionsProps>) {
  return (
    <div className="flex flex-col gap-3 pt-2 sm:flex-row sm:items-center sm:justify-between">
      <div className="flex flex-wrap gap-3">
        <Button type="submit" variant="success" disabled={isSubmitting}>
          {isSubmitting ? "Salvando..." : "Salvar alterações"}
        </Button>

        <ButtonLink to={`/students/${studentId}`} variant="outline">
          Cancelar
        </ButtonLink>
      </div>

      <DeleteStudentButton studentId={studentId} />
    </div>
  );
}
