import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { Button, ButtonLink } from "@/components/ui/button";
import { FormField } from "@/components/ui/form-field";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  createEventMutationRequestSchema,
  eventRequestDTOContentEnum,
  type CreateEventMutationRequestSchema,
} from "@/kubb";
import {
  useEmployeesSummary,
} from "../../employees/hooks/employeeQueries";
import { useStudentsSummary } from "../../students/hooks/use-students-query";
import { useCreateEventMutation } from "../hooks/use-event-mutations";

export function EventCreatePage() {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<CreateEventMutationRequestSchema>({
    resolver: zodResolver(createEventMutationRequestSchema),
  });

  const studentIdField = register("studentId");
  const employeeIdField = register("employeeId");

  // Opções para os dropdowns de aluno e colaborador
  const {
    data: studentsSummary,
    isLoading: isStudentsSummaryLoading,
    error: studentsSummaryError,
    refetch: refetchStudentSummary
  } = useStudentsSummary();
  const {
    data: employeesSummary,
    isLoading: isEmployeesSummaryLoading,
    error: employeesSummaryError,
    refetch: refetchEmployeeSummary
  } = useEmployeesSummary();

  const {
    mutate: createEvent,
    isPending: isSubmitting,
    error: submitError,
  } = useCreateEventMutation();

  const onSubmit = (data: CreateEventMutationRequestSchema) => {
    createEvent({ data });
  };

  const renderContent = () => {
    if (isStudentsSummaryLoading || isEmployeesSummaryLoading) {
      return (
        <div className="app-inline-loading">
          <span className="loading loading-spinner loading-sm text-primary" />
          <span>Carregando opções...</span>
        </div>
      );
    }

    if (studentsSummaryError || employeesSummaryError) {
      const error = studentsSummaryError || employeesSummaryError;
      return (
        <div className="space-y-3">
          <div className="alert alert-error text-sm">
            {getFriendlyErrorMessage(error)}
          </div>
          <Button
            type="button"
            onClick={() => {
              refetchStudentSummary();
              refetchEmployeeSummary();
            }}
            variant="primary"
          >
            Tentar novamente
          </Button>
        </div>
      );
    }

    return (
      <form className="flex flex-col gap-5" onSubmit={handleSubmit(onSubmit)}>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-5">
          <FormField
            className="flex flex-col gap-2"
            label="Título"
            htmlFor="title"
            error={errors.title?.message}
          >
            <input
              className="app-input"
              id="title"
              placeholder="Ex: Aula de matemática"
              {...register("title")}
            />
          </FormField>

          <FormField
            className="flex flex-col gap-2"
            label="Aluno"
            htmlFor="studentId"
            error={errors.studentId?.message}
          >
            <select
              id="studentId"
              className="app-select"
              {...studentIdField}
              onChange={(event) => {
                studentIdField.onChange(event);
                setValue("studentId", event.target.value, {
                  shouldValidate: true,
                });
              }}
              defaultValue=""
            >
              <option value="" disabled>
                Selecione um aluno
              </option>
              {studentsSummary?.map((student) => (
                <option key={student.id} value={student.id}>
                  {student.name}
                </option>
              ))}
            </select>
          </FormField>

          <FormField
            className="flex flex-col gap-2"
            label="Colaborador"
            htmlFor="employeeId"
            error={errors.employeeId?.message}
          >
            <select
              id="employeeId"
              className="app-select"
              {...employeeIdField}
              onChange={(event) => {
                employeeIdField.onChange(event);
                setValue("employeeId", event.target.value, {
                  shouldValidate: true,
                });
              }}
              defaultValue=""
            >
              <option value="" disabled>
                Selecione um colaborador
              </option>
              {employeesSummary?.map((employee) => (
                <option key={employee.id} value={employee.id}>
                  {employee.name}
                </option>
              ))}
            </select>
          </FormField>

          <FormField
            className="flex flex-col gap-2"
            label="Conteúdo"
            htmlFor="content"
            error={errors.content?.message}
          >
            <select
              id="content"
              className="app-select"
              {...register("content")}
              defaultValue=""
            >
              <option value="" disabled>
                Selecione um conteúdo
              </option>
              {Object.values(eventRequestDTOContentEnum).map((content) => (
                <option key={content} value={content}>
                  {eventRequestDTOContentEnum[content]}
                </option>
              ))}
            </select>
          </FormField>

          <FormField
            className="flex flex-col gap-2"
            label="Início"
            htmlFor="startDate"
            error={errors.startDate?.message}
          >
            <input
              className="app-input"
              id="startDate"
              type="datetime-local"
              {...register("startDate")}
            />
          </FormField>

          <FormField
            className="flex flex-col gap-2"
            label="Fim"
            htmlFor="endDate"
            error={errors.endDate?.message}
          >
            <input
              className="app-input"
              id="endDate"
              type="datetime-local"
              {...register("endDate")}
            />
          </FormField>

          <FormField
            className="flex flex-col gap-2"
            label="Preço (receita)"
            htmlFor="price"
            error={errors.price?.message}
          >
            <input
              className="app-input"
              id="price"
              type="number"
              step="0.01"
              min="0"
              {...register("price", {
                valueAsNumber: true,
                required: "Preço é obrigatório",
              })}
            />
          </FormField>

          <FormField
            className="flex flex-col gap-2"
            label="Pagamento (custo)"
            htmlFor="payment"
            error={errors.payment?.message}
          >
            <input
              className="app-input"
              id="payment"
              type="number"
              step="0.01"
              min="0"
              {...register("payment", {
                valueAsNumber: true,
                required: "Pagamento é obrigatório",
              })}
            />
          </FormField>

          <FormField
            className="flex flex-col gap-2 md:col-span-2"
            label="Descrição (opcional)"
            htmlFor="description"
            error={errors.description?.message}
          >
            <textarea
              className="app-textarea"
              id="description"
              placeholder="Observações do atendimento"
              {...register("description")}
            />
          </FormField>
        </div>

        {submitError ? (
          <div className="alert alert-error text-sm">
            {getFriendlyErrorMessage(submitError)}
          </div>
        ) : null}

        <div className="flex flex-col-reverse sm:flex-row sm:justify-end gap-3 mt-1">
          <ButtonLink to="/events" variant="outline">
            Cancelar
          </ButtonLink>
          <Button type="submit" disabled={isSubmitting} variant="primary">
            {isSubmitting ? "Salvando..." : "Criar evento"}
          </Button>
        </div>
      </form>
    );
  };

  return (
    <div className="flex flex-col gap-7">
      <PageHeader
        title="Novo evento"
        description="Crie um novo atendimento/aula."
        action={
          <ButtonLink to="/events" variant="outline">
            Voltar para eventos
          </ButtonLink>
        }
      />

      <SectionCard
        title="Dados do evento"
        description="Informe data, valores e participantes do atendimento."
      >
        {renderContent()}
      </SectionCard>
    </div>
  );
}
