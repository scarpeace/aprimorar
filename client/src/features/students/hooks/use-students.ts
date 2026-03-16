import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { useNavigate } from "react-router-dom"
import { queryKeys } from "@/lib/query/queryKeys"
import { studentsApi } from "@/services/api"
import type { StudentFormInput } from "@/lib/schemas"

export function useStudentsQuery(page = 0, size = 10, sortBy = "name") {
  const params = { page, size, sortBy }
  return useQuery({
    queryKey: queryKeys.students.list(params),
    queryFn: () => studentsApi.list(page, size, sortBy),
  })
}

export function useStudentDetailQuery(id: string) {
  return useQuery({
    queryKey: queryKeys.students.detail(id),
    queryFn: () => studentsApi.getById(id),
    enabled: Boolean(id),
  })
}

export function useCreateStudent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: StudentFormInput) => studentsApi.create(data),
    onSuccess: (createdStudent) => {
      queryClient.invalidateQueries({ queryKey: queryKeys.students.lists() })
      queryClient.invalidateQueries({ queryKey: queryKeys.parents.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all })
      navigate(`/students/${createdStudent.id}`)
    },
  })
}

export function useUpdateStudent(id: string) {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: StudentFormInput) => studentsApi.update(id, data),
    onSuccess: async (updatedStudent) => {
      // 1. Navigate first
      navigate(`/students/${updatedStudent.id}`)

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

export function useDeleteStudent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (id: string) => studentsApi.delete(id),
    onSuccess: async (_, id) => {
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
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.students.all }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all }),
      ])
    },
  })
}

export function useUnarchiveStudent() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => studentsApi.unarchive(id),
    onSuccess: async (_, id) => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.students.all }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all }),
      ])
    },
  })
}

export function useStudentOptionsQuery() {
  return useQuery({
    queryKey: queryKeys.students.options,
    queryFn: () => studentsApi.getOptions(),
  })
}
