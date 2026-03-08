import { Link } from "react-router-dom"
import { useNavigate } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import styles from "@/features/employees/EmployeeCreatePage.module.css"
import { useForm } from "react-hook-form"
import { useHookFormMask } from "use-mask-input"
import { zodResolver } from "@hookform/resolvers/zod"
import { createEmployeeSchema, type CreateEmployeeInput } from "@/lib/schemas/employee"
import { employeesApi, getFriendlyErrorMessage } from "@/services/api"
import { useState } from "react"

export function EmployeeCreatePage() {
  const navigate = useNavigate()
  const [submitError, setSubmitError] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<CreateEmployeeInput>({
    resolver: zodResolver(createEmployeeSchema),
    defaultValues: {
      role: "EMPLOYEE",
    },
  })

  const onSubmit = async (data: CreateEmployeeInput) => {
    try {
      setSubmitError(null)
      const res = await employeesApi.create(data)
      navigate(`/employees/${res.data.id}`)
    } catch (error) {
      console.error("Falha ao criar colaborador:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    }
  }

  const registerWithMask = useHookFormMask(register)

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Novo colaborador</h1>
          <p className="text-sm text-gray-600">Crie um novo cadastro de colaborador.</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/employees">← Voltar para colaboradores</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Dados do colaborador</CardTitle>
          <CardDescription>Preencha as informações abaixo para criar o cadastro.</CardDescription>
        </CardHeader>
        <CardContent>
          <form className={styles.form} onSubmit={handleSubmit(onSubmit)} autoComplete="off">
            <div className={styles.formGrid}>
              <div className={styles.field}>
                <label className={styles.label} htmlFor="name">
                  Nome completo
                </label>
                <Input id="name" placeholder="Ex: Maria Silva" {...register("name")} />
                {errors.name?.message ? <p className={styles.error}>{errors.name.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="birthdate">
                  Data de nascimento
                </label>
                <Input id="birthdate" type="date" {...register("birthdate")} />
                {errors.birthdate?.message ? (
                  <p className={styles.error}>{errors.birthdate.message}</p>
                ) : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="email">
                  E-mail
                </label>
                <Input id="email" type="email" placeholder="exemplo@dominio.com" {...register("email")} />
                {errors.email?.message ? <p className={styles.error}>{errors.email.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="contact">
                  Contato
                </label>
                <Input
                  id="contact"
                  placeholder="(11) 99999-9999"
                  {...registerWithMask("contact", ["(99) 9999-9999", "(99) 99999-9999"])}
                />
                {errors.contact?.message ? <p className={styles.error}>{errors.contact.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="cpf">
                  CPF
                </label>
                <Input
                  id="cpf"
                  placeholder="000.000.000-00"
                  {...registerWithMask("cpf", "999.999.999-99")}
                />
                {errors.cpf?.message ? <p className={styles.error}>{errors.cpf.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="pix">
                  Chave PIX
                </label>
                <Input id="pix" placeholder="cpf/email/telefone/chave aleatoria" {...register("pix")} />
                {errors.pix?.message ? <p className={styles.error}>{errors.pix.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="role">
                  Perfil
                </label>
                <select
                  id="role"
                  className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                  {...register("role")}
                >
                  <option value="EMPLOYEE">Colaborador</option>
                  <option value="ADMIN">Administrador</option>
                </select>
                {errors.role?.message ? <p className={styles.error}>{errors.role.message}</p> : null}
              </div>
            </div>

            {submitError ? (
              <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
                {submitError}
              </div>
            ) : null}

            <div className={styles.actions}>
              <Button asChild type="button" variant="outline">
                <Link to="/employees">Cancelar</Link>
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Salvando..." : "Criar colaborador"}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}
