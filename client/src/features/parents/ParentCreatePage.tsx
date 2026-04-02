// import { zodResolver } from "@hookform/resolvers/zod";
// import { useForm } from "react-hook-form";
// import { useHookFormMask } from "use-mask-input";
// import { Button, ButtonLink } from "@/components/ui/button";
// import { FormField } from "@/components/ui/form-field";
// import { PageHeader } from "@/components/ui/page-header";
// import { SectionCard } from "@/components/ui/section-card";
// import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
// import { Alert } from "@/components/ui/alert";
// import { LoadingSpinner } from "@/components/ui/loading-spinner";
// import { GraduationCap } from "lucide-react";
// import { ParentFormFields } from "./components/ParentFormFields";

// //TODO: Eu acho melhor separar todos os formulários de criação como eu tinha pensado antes e criar um "Matricular Aluno"
// export function ParentCreatePage() {
//   const {
//     isPending: isCreateParentPending,
//     isError: isCreateParentError,
//     error: createParentError,
//   } = useCreateParent();

//   const {
//     register,
//     handleSubmit,
//     formState: { errors },
//   } = useForm<CreateParentMutationRequestSchema>({
//     resolver: zodResolver(createParentMutationRequestSchema),
//   });
//   const registerWithMask = useHookFormMask(register);

//   const onSubmit = (data: CreateParentMutationRequestSchema) => {
//     createParent( data );
//   };

//    return (
//     <>
//       <PageHeader
//         title="Criar aluno"
//         description="Preencha os dados do aluno e do responsável."
//         Icon={GraduationCap}
//       />

//       <div className="container flex flex-col gap-7">
//         <ParentForm onSubmit={onSubmit}>


//           <ParentFormFields
//             register={register}
//             registerWithMask={registerWithMask}
//             prefix="parent"
//             errors={errors.parent}
//             className="grid grid-cols-2 gap-4"
//           />

//            {isCreateParentError && (
//             <Alert error={createParentError} variant="error" />
//           )}

//           <div className="flex justify-end flex-wrap gap-3">
//             <Button type="submit" variant="success"disabled={isCreateParentPending}>
//               {isCreateParentPending ? <LoadingSpinner text={"Salvando"} /> : "Salvar alterações"}
//             </Button>

//             <ButtonLink to={`/parents/`} variant="outline">
//               Cancelar
//             </ButtonLink>
//           </div>
//         </ParentForm>
//       </div>
//     </>
//   );
// }
