import { Alert } from "@/components/ui/alert";
import { Button, ButtonLink } from "@/components/ui/button";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { PageHeader } from "@/components/ui/page-header";
import { zodResolver } from "@hookform/resolvers/zod";
import { GraduationCap } from "lucide-react";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { ParentForm } from "../forms/ParentForm";
import { ParentFormFields } from "../forms/ParentFormFields";
import {
  parentFormSchema,
  type ParentFormSchema,
} from "../forms/parentFormSchema";
import { useParentMutations } from "../hooks/parent-mutations";

export function ParentCreatePage() {
  const {
    register,
    formState: { errors },
    handleSubmit,
  } = useForm<ParentFormSchema>({
    resolver: zodResolver(parentFormSchema),
    mode: "onBlur",
  });
  const registerWithMask = useHookFormMask(register);

  const {
    createParent: {
      mutate: createParent,
      isPending: isCreateParentPending,
      isError: isCreateParentError,
      error: createParentError,
    },
  } = useParentMutations();

  const onSubmit = handleSubmit((data: ParentFormSchema) => {
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
