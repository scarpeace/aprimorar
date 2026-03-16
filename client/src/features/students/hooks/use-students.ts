import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { useNavigate } from "react-router-dom"
import { queryKeys } from "@/lib/query/queryKeys"
import { studentsApi, parentsApi } from "@/services/api"
import type { StudentFormInput } from "@/lib/schemas"

export function useStudentsQuery(page = 0, size = 20, sortBy = "name") {
  return useQuery({
    queryKey: [...queryKeys.students, { page, size, sortBy }],
    queryFn: async () => {
      const response = await studentsApi.list(page, size, sortBy)
      return response.content
    },
  })
}

export function useStudentDetailQuery(id: string) {
  return useQuery({
    queryKey: queryKeys.studentDetail(id),
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
      queryClient.invalidateQueries({ queryKey: queryKeys.students })
      queryClient.invalidateQueries({ queryKey: queryKeys.parents })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard })
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
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.students }),
        queryClient.invalidateQueries({ queryKey: queryKeys.studentDetail(id) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.parents }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events }),
      ])
      navigate(`/students/${updatedStudent.id}`)
    },
  })
}

export function useDeleteStudent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (id: string) => studentsApi.delete(id),
    onSuccess: async (_, id) => {
      // Remove specific student from cache to avoid flicker/404 on refetch
      queryClient.removeQueries({ queryKey: queryKeys.studentDetail(id) })

      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.students }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
      ])
      navigate("/students")
    },
  })
}

export function useArchiveStudent() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => studentsApi.archive(id),
    onSuccess: async (_, id) => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.students }),
        queryClient.invalidateQueries({ queryKey: queryKeys.studentDetail(id) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
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
        queryClient.invalidateQueries({ queryKey: queryKeys.students }),
        queryClient.invalidateQueries({ queryKey: queryKeys.studentDetail(id) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
      ])
    },
  })
}

export function useStudentOptionsQuery() {
  return useQuery({
    queryKey: [...queryKeys.students, "options"],
    queryFn: () => studentsApi.getOptions(),
  })
}
