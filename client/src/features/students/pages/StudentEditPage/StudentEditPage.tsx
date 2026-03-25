import { Alert } from "@/components/ui/alert";
import { ButtonLink } from "@/components/ui/button";
import { PageHeader } from "@/components/ui/page-header";
import { useParams, useNavigate } from "react-router-dom";
import { useStudentById } from "../../query/studentQueries";
import { useUpdateStudentMutation } from "../../query/studentMutations";
import type { UpdateStudentMutationRequestSchema } from "@/kubb";

export function StudentEditPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const navigate = useNavigate();

  const {
    data: student,
    isLoading: isStudentLoading,
    error: studentError,
    refetch: refetchStudent,
  } = useStudentById(studentId);

  const form = useStudentEditForm(student);
  const {
    mutate: updateStudent,
    isPending:isUpdatingStudent,
    error: updateStudentError,
  } = useUpdateStudentMutation();

  const onSubmit = form.handleSubmit((data: UpdateStudentMutationRequestSchema) => {
    updateStudent({ studentId, ...data }, {
      onSuccess: () => navigate(`/students/${studentId}`),
    });
  });

  return (
    <div className={styles.page}>
      <PageHeader
        title="Editar aluno"
        description="Atualize os dados do aluno e do responsável."
        action={
          <ButtonLink to={`/students/${studentId}`} variant="outline">
            Voltar para detalhes
          </ButtonLink>
        }
      />

      <StudentEditState
        studentId={studentId}
        student={student}
        isLoading={isStudentLoading}
        error={studentError}
        onRetry={refetchStudent}
      >
        <StudentEditForm onSubmit={onSubmit}>
          <StudentResponsibleSection
            selectedParentId={form.selectedParentId}
            setValue={form.setValue}
            register={form.register}
            errors={form.errors}
          />

          <StudentInfoSection
            register={form.register}
            registerWithMask={form.registerWithMask}
            errors={form.errors}
          />

          <StudentAddressSection
            register={form.register}
            errors={form.errors}
          />

          <StudentEditActions
            studentId={studentId}
            isSubmitting={isUpdating}
          />

          {updateStudentError ? (
            <Alert
              variant="error"
              message="Não foi possível atualizar o aluno."
            />
          ) : null}
        </StudentEditForm>
      </StudentEditState>
    </div>
  );
}
