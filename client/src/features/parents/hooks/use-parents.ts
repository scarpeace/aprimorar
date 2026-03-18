import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { useNavigate } from "react-router-dom"
import { parentsApi, studentsApi } from "@/services/api"
import { queryKeys } from "@/lib/query/queryKeys"
import type { ParentFormInput } from "@/lib/schemas"
import { toast } from "sonner"

// --- QUERIES ---

export function useParentsQuery(page = 0, size = 20, search?: string, sortBy = "name") {
  return useQuery({
    queryKey: queryKeys.parents.list({ page, size, sortBy, search }),
    queryFn: () => parentsApi.listPaginated(page, size, sortBy, search),
  })
}

export function useParentsListQuery() {
  return useQuery({
    queryKey: queryKeys.parents.options,
    queryFn: () => parentsApi.list(),
  })
}

export function useParentDetailQuery(id: string) {
  return useQuery({
    queryKey: queryKeys.parents.detail(id),
    queryFn: () => parentsApi.getById(id),
    enabled: !!id,
  })
}

export function useStudentsByParentQuery(parentId: string, options = {}) {
  return useQuery({
    queryKey: queryKeys.students.byParent(parentId),
    queryFn: () => studentsApi.listByParent(parentId),
    enabled: !!parentId,
    ...options
  })
}

// --- MUTATIONS ---

export function useCreateParent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: ParentFormInput) => parentsApi.create(data),
    onSuccess: (createdParent) => {
      toast.success("Responsável criado com sucesso!")
      queryClient.invalidateQueries({ queryKey: queryKeys.parents.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all })
      navigate(`/parents/${createdParent.id}`)
    },
  })
}

export function useUpdateParent(id: string) {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: ParentFormInput) => parentsApi.update(id, data),
    onSuccess: (updatedParent) => {
      toast.success("Responsável atualizado com sucesso!")
      queryClient.invalidateQueries({ queryKey: queryKeys.parents.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.parents.detail(id) })
      navigate(`/parents/${id}`)
    },
  })
}

export function useDeleteParent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (id: string) => parentsApi.delete(id),
    onSuccess: async (_, id) => {
      toast.success("Responsável deletado com sucesso!")
      navigate("/parents")
      queryClient.invalidateQueries({ queryKey: queryKeys.parents.lists() })
      queryClient.invalidateQueries({ queryKey: ["students", "by-parent"] })
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.all })
    },
  })
}

export function useArchiveParent() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => parentsApi.archive(id),
    onSuccess: (_, id) => {
      toast.success("Responsável arquivado com sucesso!")
      queryClient.invalidateQueries({ queryKey: queryKeys.parents.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.parents.detail(id) })
    },
  })
}

export function useUnarchiveParent() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => parentsApi.unarchive(id),
    onSuccess: (_, id) => {
      toast.success("Responsável desarquivado com sucesso!")
      queryClient.invalidateQueries({ queryKey: queryKeys.parents.all })
      queryClient.invalidateQueries({ queryKey: queryKeys.parents.detail(id) })
    },
  })
}
