import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { useNavigate } from "react-router-dom"
import { employeesApi, eventsApi } from "@/services/api"
import { queryKeys } from "@/lib/query/queryKeys"
import type { EmployeeFormInput } from "@/lib/schemas"

// --- QUERIES ---

export function useEmployeesQuery(page = 0, size = 20, sortBy = "name") {
  return useQuery({
    queryKey: [...queryKeys.employees, "list", { page, size, sortBy }],
    queryFn: () => employeesApi.list(page, size, sortBy),
  })
}

export function useEmployeeDetailQuery(id: string) {
  return useQuery({
    queryKey: [...queryKeys.employees, id],
    queryFn: () => employeesApi.getById(id),
    enabled: !!id,
  })
}

export function useEmployeeEditQuery(id: string) {
  return useQuery({
    queryKey: [...queryKeys.employees, "edit", id],
    queryFn: () => employeesApi.getByIdForEdit(id),
    enabled: !!id,
  })
}

export function useEmployeeEventsQuery(id: string, page = 0, size = 20) {
  return useQuery({
    queryKey: [...queryKeys.events, "employee", id, { page, size }],
    queryFn: () => eventsApi.listByEmployee(id, page, size),
    enabled: !!id,
  })
}

export function useEmployeeOptionsQuery() {
  return useQuery({
    queryKey: [...queryKeys.employees, "options"],
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
      queryClient.invalidateQueries({ queryKey: queryKeys.employees })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard })
      navigate(`/employees/${createdEmployee.id}`)
    },
  })
}

export function useUpdateEmployee(id: string) {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: EmployeeFormInput) => employeesApi.update(id, data),
    onSuccess: (updatedEmployee) => {
      queryClient.invalidateQueries({ queryKey: queryKeys.employees })
      queryClient.setQueryData([...queryKeys.employees, id], updatedEmployee)
      navigate(`/employees/${id}`)
    },
  })
}

export function useDeleteEmployee() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (id: string) => employeesApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.employees })
      navigate("/employees")
    },
  })
}

export function useArchiveEmployee() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => employeesApi.archive(id),
    onSuccess: (_, id) => {
      queryClient.invalidateQueries({ queryKey: queryKeys.employees })
      queryClient.invalidateQueries({ queryKey: [...queryKeys.employees, id] })
    },
  })
}

export function useUnarchiveEmployee() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => employeesApi.unarchive(id),
    onSuccess: (_, id) => {
      queryClient.invalidateQueries({ queryKey: queryKeys.employees })
      queryClient.invalidateQueries({ queryKey: [...queryKeys.employees, id] })
    },
  })
}
