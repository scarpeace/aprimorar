import { PageLayout } from "@/components/layout/PageLayout";
import { zodResolver } from "@hookform/resolvers/zod";
import { GraduationCap, TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";

import { Alert } from "@/components/ui/alert";
import { Button, ButtonLink } from "@/components/ui/button";
import { SectionCard } from "@/components/ui/section-card";
import { ParentSelectDropdown } from "@/features/parents/components/ParentSelectDropdown";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { brazilianStates } from "@/lib/utils/brazilianStates";
import { useHookFormMask } from "use-mask-input";
import { studentFormSchema, type StudentFormSchema } from "../forms/studentFormSchema";
import { useStudentMutations } from "../hooks/student-mutations";

export function StudentCreatePage() {
  const { register, handleSubmit, control, formState: { errors } } = useForm<StudentFormSchema>({
    resolver: zodResolver(studentFormSchema),
    mode: "onBlur",
  });
  const registerWithMask = useHookFormMask(register);

  const { createStudent } = useStudentMutations();

  const onSubmit = handleSubmit((data: StudentFormSchema) => {
    createStudent.mutate({ data });
  });

  const headerProps = {
    title: "Novo Aluno",
    description: "Preencha os dados do aluno no formulário abaixo.",
    Icon: GraduationCap,
    backLink: "/students",
  };

  return (
    <PageLayout {...headerProps}>

      <SectionCard title={"Cadastre um novo aluno"} description={"Informe os dados do aluno e selecione um responsável."}>

        {createStudent.isError && (
            <Alert error={getFriendlyErrorMessage(createStudent.error)} variant="error" />
        )}

        <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">
            <ParentSelectDropdown className="col-span-3" label="Responsável" control={control} error={errors.parentId?.message} />

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Nome</legend>
            <input type="text" className="input" {...register("name")} placeholder="Nome Completo"/>
            {errors?.name && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.name.message}</p>)}
          </fieldset>

           <fieldset className="fieldset">
            <legend className="fieldset-legend">Data de Nascimento</legend>
            <input type="text" className="input" {...registerWithMask("birthdate",["##/##/####"])} placeholder="Ex: 01/01/1990"/>
            {errors?.birthdate && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.birthdate.message}</p>)}
          </fieldset>

           <fieldset className="fieldset">
            <legend className="fieldset-legend">CPF</legend>
            <input type="text" className="input" placeholder="Ex: 123.456.789-00" {...registerWithMask("cpf", ["###.###.###-##"])}/>
            {errors?.cpf && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.cpf.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Contato</legend>
              <input type="text" className="input" placeholder="Ex: (61) 99633-2332"
                {...registerWithMask("contact", ["(##) #####-####", "(##) ####-####"])}
              />
            {errors?.contact && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.contact.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Email</legend>
            <input type="text" className="input" {...register("email")} placeholder="email@email.com"/>
            {errors?.email && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.email.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Escola</legend>
            <input type="text" className="input" {...register("school")} placeholder="Ex: Leonardo da Vinci"/>
            {errors?.school && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.school.message}</p>)}
          </fieldset>

            <div className="divider col-span-3 m-0" />

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Rua</legend>
            <input type="text" className="input" {...register("address.street")} placeholder="Ex: SQS 406, Bloco C"/>
            {errors?.address?.street && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.street.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Bairro</legend>
            <input type="text" className="input" {...register("address.district")} placeholder="Ex: Asa sul"/>
            {errors?.address?.district && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.district.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Complemento</legend>
            <input type="text" className="input" {...register("address.complement")} placeholder="Ex: Apto 101"/>
            {errors?.address?.complement && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.complement.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Cidade</legend>
            <input type="text" className="input" {...register("address.city")} placeholder="Ex: Brasília"/>
            {errors?.address?.city && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.city.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Estado</legend>
             <select className="select select-bordered w-full" {...register("address.state")}>
                {Object.values(brazilianStates).map((content) => (
                    <option key={content} value={content}>
                        {content}
                    </option>
                ))}
            </select>
            {errors?.address?.state && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.state.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">CEP</legend>
            <input type="text" className="input" {...registerWithMask("address.zip", ["#####-###"])} placeholder="Ex: 70254-010"/>
            {errors?.address?.zip && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.zip.message}</p>)}
          </fieldset>
        </div>

        <div className="mt-1 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <ButtonLink to={"/students"} variant="outline">Cancelar</ButtonLink>
          <Button type="submit" disabled={createStudent.isPending} variant="primary">
            {createStudent.isPending ? "Salvando..." : "Salvar"}
          </Button>
        </div>
      </form>
      </SectionCard>
    </PageLayout>
  );
}
