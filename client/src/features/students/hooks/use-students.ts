import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { useNavigate } from "react-router-dom"
import { queryKeys } from "@/lib/query/queryKeys"
import { studentsApi } from "@/services/api"
import type { StudentFormInput } from "@/lib/schemas"
import { toast } from "sonner"

export function useUpdateStudent(id: string) {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: StudentFormInput) => studentsApi.update(id, data),
    onSuccess: async (updatedStudent) => {
      // 1. Navigate first
      navigate(`/students/${updatedStudent.id}`)
      toast.success("Aluno atualizado com sucesso!")
      // 2. Invalidate queries
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.students.lists() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.students.detail(id) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.parents.all }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.all }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all }),
      ])
    },
  })
}

export function useCreateStudent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: StudentFormInput) => studentsApi.create(data),
    onSuccess: (createdStudent) => {
      toast.success("Aluno criado com sucesso!")
      queryClient.invalidateQueries({ queryKey: queryKeys.students.lists() })
      queryClient.invalidateQueries({ queryKey: queryKeys.parents.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all })
      navigate(`/students/${createdStudent.id}`)

    },
  })
}

export function useDeleteStudent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (id: string) => studentsApi.delete(id),
    onSuccess: async (_, id) => {
      toast.success("Aluno deletado com sucesso!")
      navigate("/students")
      queryClient.invalidateQueries({ queryKey: queryKeys.students.lists() })
      queryClient.invalidateQueries({ queryKey: ["students", "by-parent"] })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all })
    },
  })
}

export function useArchiveStudent() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => studentsApi.archive(id),
    onSuccess: async (_, id) => {
      toast.success("Aluno arquivado com sucesso!")
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.students.all }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all }),
      ])
    },
  })
}

export function useStudentsQuery(page: number, size: number, search?: string, options = {}) {
  return useQuery({
    queryKey: queryKeys.students.list({ page, size, search }),
    queryFn: () => studentsApi.list(page, size, "name", search),
    staleTime: 1000 * 60 * 5,
    ...options
  })
}

export function useStudentDetailQuery(id: string) {
  return useQuery({
    queryKey: queryKeys.students.detail(id),
    queryFn: () => studentsApi.getById(id),
    enabled: Boolean(id),
  })
}

export function useStudentOptionsQuery() {
  return useQuery({
    queryKey: queryKeys.students.options,
    queryFn: () => studentsApi.getOptions(),
  })
}

export function useUnarchiveStudent() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => studentsApi.unarchive(id),
    onSuccess: async (_, id) => {
      toast.success("Aluno desarquivado com sucesso!")
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.students.all }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all }),
      ])
    },
  })
}


