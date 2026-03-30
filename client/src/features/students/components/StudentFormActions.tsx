import { Button, ButtonLink } from "@/components/ui/button";

type StudentFormActionsProps = {
  isSubmitting: boolean;
};

export function StudentFormActions({
  isSubmitting,
}: Readonly<StudentFormActionsProps>) {
  return (
    <div className="flex justify-end">
      <div className="flex flex-col gap-3 pt-2 sm:flex-row sm:items-center sm:justify-between">
        <div className="flex flex-wrap gap-3">
          <Button type="submit" variant="success" disabled={isSubmitting}>
            {isSubmitting ?
              <span className="loading loading-spinner loading-xs"/>
              : "Salvar alterações"}
          </Button>

          <ButtonLink to={`/students/`} variant="outline">
            Cancelar
          </ButtonLink>
        </div>
      </div>
    </div>
  );
}
