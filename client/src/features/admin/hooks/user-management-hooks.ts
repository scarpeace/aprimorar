import { api } from "@/lib/shared/api";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";

export type UserResponse = {
  id: string;
  username: string;
  employeeName: string;
  role: "ADMIN" | "SECRETARY" | "TEACHER" | "MENTOR";
  active: boolean;
};

export type UserCreateRequest = {
  employeeId: string;
  username: string;
  password: string;
  role: string;
};

export const getUsersQueryKey = () => ["/v1/admin/users"];

export function useUsers() {
  return useQuery({
    queryKey: getUsersQueryKey(),
    queryFn: async () => {
      const response = await api.get<UserResponse[]>("/v1/admin/users");
      return response.data;
    },
  });
}

export function useUserMutations() {
  const queryClient = useQueryClient();

  const createUser = useMutation({
    mutationFn: async (data: UserCreateRequest) => {
      const response = await api.post<UserResponse>("/v1/admin/users", data);
      return response.data;
    },
    onSuccess: () => {
      toast.success("Usuário criado com sucesso");
      queryClient.invalidateQueries({ queryKey: getUsersQueryKey() });
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error));
    },
  });

  const deleteUser = useMutation({
    mutationFn: async (id: string) => {
      await api.delete(`/v1/admin/users/${id}`);
    },
    onSuccess: () => {
      toast.success("Usuário removido com sucesso");
      queryClient.invalidateQueries({ queryKey: getUsersQueryKey() });
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error));
    },
  });

  return { createUser, deleteUser };
}
