import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { useNavigate } from "react-router-dom"
import { toast } from "sonner"
import { employeesApi, eventsApi, getFriendlyErrorMessage } from "@/services/api"
import { queryKeys } from "@/lib/query/queryKeys"
import type { EmployeeFormInput } from "@/lib/schemas"

// --- QUERIES ---

export function useEmployeesQuery(page = 0, size = 20, search?: string, sortBy = "name") {
  return useQuery({
    queryKey: queryKeys.employees.list({ page, size, sortBy, search }),
    queryFn: () => employeesApi.list(page, size, sortBy, search),
  })
}

export function useEmployeeDetailQuery(id: string) {
  return useQuery({
    queryKey: queryKeys.employees.detail(id),
    queryFn: () => employeesApi.getById(id),
    enabled: !!id,
  })
}

export function useEmployeeEditQuery(id: string) {
  return useQuery({
    queryKey: queryKeys.employees.detail(id),
    queryFn: () => employeesApi.getByIdForEdit(id),
    enabled: !!id,
  })
}

export function useEmployeeOptionsQuery() {
  return useQuery({
    queryKey: queryKeys.employees.options,
    queryFn: () => employeesApi.getOptions(),
  })
}

// --- MUTATIONS ---

export function useCreateEmployee() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: EmployeeFormInput) => employeesApi.create(data),
    onSuccess: (createdEmployee) => {
      toast.success("Colaborador criado com sucesso!")
      queryClient.invalidateQueries({ queryKey: queryKeys.employees.lists() })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all })
      navigate(`/employees/${createdEmployee.id}`)
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error))
    },
  })
}

export function useUpdateEmployee(id: string) {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: EmployeeFormInput) => employeesApi.update(id, data),
    onSuccess: (updatedEmployee) => {
      toast.success("Colaborador atualizado com sucesso!")
      queryClient.invalidateQueries({ queryKey: queryKeys.employees.lists() })
      queryClient.invalidateQueries({ queryKey: queryKeys.employees.detail(id) })
      navigate(`/employees/${id}`)
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error))
    },
  })
}

export function useDeleteEmployee() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (id: string) => employeesApi.delete(id),
    onSuccess: async (_, id) => {
      toast.success("Colaborador excluído com sucesso!")
      navigate("/employees")
      queryClient.invalidateQueries({ queryKey: queryKeys.employees.lists() })
      queryClient.invalidateQueries({ queryKey: ["events", "by-employee"] })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all })
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error))
    },
  })
}

export function useArchiveEmployee() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => employeesApi.archive(id),
    onSuccess: (_, id) => {
      toast.success("Colaborador arquivado com sucesso!")
      queryClient.invalidateQueries({ queryKey: queryKeys.employees.lists() })
      queryClient.invalidateQueries({ queryKey: queryKeys.employees.detail(id) })
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error))
    },
  })
}

export function useUnarchiveEmployee() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => employeesApi.unarchive(id),
    onSuccess: (_, id) => {
      toast.success("Colaborador desarquivado com sucesso!")
      queryClient.invalidateQueries({ queryKey: queryKeys.employees.lists() })
      queryClient.invalidateQueries({ queryKey: queryKeys.employees.detail(id) })
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error))
    },
  })
}
