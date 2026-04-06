import { Alert } from "@/components/ui/alert";
import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { PageHeader } from "@/components/ui/page-header";
import { useGetParentById } from "@/kubb";
import { DevTool } from "@hookform/devtools";
import { zodResolver } from "@hookform/resolvers/zod";
import { Handshake } from "lucide-react";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import { useHookFormMask } from "use-mask-input";
import { ParentForm } from "../forms/ParentForm";
import { ParentFormFields } from "../forms/ParentFormFields";
import {
  parentFormSchema,
  type ParentFormSchema,
} from "../forms/parentFormSchema";
import { useUpdateParentMutation } from "../hooks/parent-mutations";

export function ParentEditPage() {
  const { id } = useParams<{ id: string }>();
  const parentId = id ?? "";

  const {
    isPending: isParentPending,
    error: parentError,
    data: parentData,
  } = useGetParentById(parentId);

  const {
    mutate: updateParent,
    isPending: isUpdatingParent,
    error: updateParentError,
  } = useUpdateParentMutation();

  const {
    register,
    handleSubmit,
    formState: { errors },
    control,
  } = useForm<ParentFormSchema>({
    resolver: zodResolver(parentFormSchema),
    values: {
      name: parentData?.name ?? "",
      email: parentData?.email ?? "",
      contact: parentData?.contact ?? "",
      cpf: parentData?.cpf ?? "",
    },
  });
  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: ParentFormSchema) => {
    updateParent({ parentId, data });
  });

  return (
    <>
      <PageHeader
        title="Editar responsável"
        description="Atualize os dados do responsável."
        backLink={`/parents/${parentId}`}
        Icon={Handshake}
      />
      <DevTool control={control} />

      {parentError ? (
        <ErrorCard
          title="Erro ao carregar detalhes do responsável"
          error={parentError}
        />
      ) : isParentPending ? (
        <LoadingCard title="Carregando detalhes do responsável" />
      ) : (

          <div className="container animate-[fade-up_300ms_ease-out_both]">
            <ParentForm onSubmit={onSubmit}>
              <ParentFormFields
                register={register}
                registerWithMask={registerWithMask}
                errors={errors}
                className="grid grid-cols-2 gap-4"
              />

              {updateParentError && (
                <Alert error={updateParentError} variant="error" />
              )}

              <div className="flex justify-end flex-wrap gap-3">
                <Button
                  type="submit"
                  variant="success"
                  disabled={isUpdatingParent}
                >
                  {isUpdatingParent ? (
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
      )}
    </>
  );
}
