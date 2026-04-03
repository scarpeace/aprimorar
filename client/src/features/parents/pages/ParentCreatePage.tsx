import { Alert } from "@/components/ui/alert";
import { Button, ButtonLink } from "@/components/ui/button";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { PageHeader } from "@/components/ui/page-header";
import { zodResolver } from "@hookform/resolvers/zod";
import { GraduationCap } from "lucide-react";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { useCreateParent, createParentMutationRequestSchema } from "@/kubb";
import {
  parentFormInputSchema,
  type ParentFormInputSchema,
} from "../forms/parentFormSchema";
import { useCreateParentMutation } from "../hooks/use-parent-mutations";
import { ParentFormFields } from "../forms/ParentFormFields";
import { ParentForm } from "../forms/ParentForm";

export function ParentCreatePage() {
  const {
    register,
    formState: { errors },
    handleSubmit,
  } = useForm<ParentFormInputSchema>({
    resolver: zodResolver(parentFormInputSchema),
    mode: "onBlur",
  });
  const registerWithMask = useHookFormMask(register);

  const {
    mutate: createParent,
    isPending: isCreateParentPending,
    isError: isCreateParentError,
    error: createParentError,
  } = useCreateParentMutation();

  const onSubmit = handleSubmit((data: ParentFormInputSchema) => {
    createParent({ data });
  });

  return (
    <>
      <PageHeader
        title="Criar responsável"
        description="Preencha os dados do responsável."
        Icon={GraduationCap}
        backLink={"/parents"}
      />

      <div className="container animate-[fade-up_300ms_ease-out_both]">
        <ParentForm onSubmit={onSubmit}>
          <ParentFormFields
            register={register}
            registerWithMask={registerWithMask}
            errors={errors}
            className="grid grid-cols-2 gap-4"
          />

          {isCreateParentError && (
            <Alert error={createParentError} variant="error" />
          )}

          <div className="flex justify-end flex-wrap gap-3">
            <Button
              type="submit"
              variant="success"
              disabled={isCreateParentPending}
            >
              {isCreateParentPending ? (
                <LoadingSpinner text={"Salvando"} />
              ) : (
                "Salvar alterações"
              )}
            </Button>

            <ButtonLink to={`/parents/`} variant="outline">
              Cancelar
            </ButtonLink>
          </div>
        </ParentForm>
      </div>
    </>
  );
}
