import { Alert } from "@/components/ui/alert";
import { Button, ButtonLink } from "@/components/ui/button";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { PageHeader } from "@/components/ui/page-header";
import { AddressFormFields } from "@/features/address/AddressFormFields";
import { zodResolver } from "@hookform/resolvers/zod";
import { GraduationCap } from "lucide-react";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { StudentForm } from "../forms/StudentForm";
import { StudentFormFields } from "../forms/StudentFormFields";
import { studentFormInputSchema, type StudentFormInputSchema } from "../forms/studentFormSchema";
import { useCreateStudentMutation } from "../hooks/use-student-mutation";

export function StudentCreatePage() {
  const { register, formState: { errors }, handleSubmit } = useForm<StudentFormInputSchema>({
    resolver: zodResolver(studentFormInputSchema),
    mode: "onBlur",
  });
  const registerWithMask = useHookFormMask(register);

  const {
    mutate: createStudent,
    isPending: isCreateStudentPending,
    isError: isCreateStudentError,
    error: createStudentError,
  } = useCreateStudentMutation();

  const onSubmit = handleSubmit((data: StudentFormInputSchema) => {
    createStudent({ data });
  });

  return (
    <>
      <PageHeader
        title="Criar aluno"
        description="Preencha os dados do aluno e do responsável."
        Icon={GraduationCap}
        backLink="/students"
      />

      <div className="container animate-[fade-up_300ms_ease-out_both]">
        <StudentForm onSubmit={onSubmit}>

          <StudentFormFields
            register={register}
            registerWithMask={registerWithMask}
            errors={errors}
            className="grid grid-cols-3 gap-4"
          />

          {/*TODO: talvez mover isso pra dentro do student form fields pra tirar o prefix*/}
          <AddressFormFields
            register={register}
            registerWithMask={registerWithMask}
            errors={errors.address}
            prefix="address"
            className="grid grid-cols-3 gap-4"
          />

           {isCreateStudentError && (
            <Alert error={createStudentError} variant="error" />
          )}

          <div className="flex justify-end flex-wrap gap-3">
            <Button type="submit" variant="success"disabled={isCreateStudentPending}>
              {isCreateStudentPending ? <LoadingSpinner text={"Salvando"} /> : "Salvar alterações"}
            </Button>

            <ButtonLink to={`/students/`} variant="outline">
              Cancelar
            </ButtonLink>
          </div>
        </StudentForm>
      </div>
    </>
  );
}
