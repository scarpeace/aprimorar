import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import {
  useCreateParent,
  useUpdateParent,
  useArchiveParent,
  useUnarchiveParent,
} from "@/kubb";
import { parentQueryKeys } from "./parentQueryKeys";

export function useCreateParentMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useCreateParent({
    mutation: {
      onSuccess: (createdParent) => {
        toast.success("Responsável criado com sucesso");
        queryClient.invalidateQueries({
          queryKey: parentQueryKeys.lists(),
        });
        queryClient.invalidateQueries({
          queryKey: parentQueryKeys.summary(),
        });
        navigate(`/parents/${createdParent.parentId}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}

export function useUpdateParentMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useUpdateParent({
    mutation: {
      onSuccess: (updatedParent, variables) => {
        toast.success("Responsável atualizado com sucesso");

        queryClient.invalidateQueries({ queryKey: parentQueryKeys.lists() });
        queryClient.invalidateQueries({
          queryKey: parentQueryKeys.detail(variables.parentId),
        });
        queryClient.invalidateQueries({
          queryKey: parentQueryKeys.summary(),
        });

        navigate(`/parents/${updatedParent.parentId}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}

export function useArchiveParentMutation() {
  const queryClient = useQueryClient();

  return useArchiveParent({
    mutation: {
      onSuccess: (archivedParent, variables) => {
        console.log(archivedParent);
        toast.success("Responsável arquivado com sucesso");

        queryClient.invalidateQueries({ queryKey: parentQueryKeys.lists() });
        queryClient.invalidateQueries({
          queryKey: parentQueryKeys.detail(variables.parentId),
        });
        queryClient.invalidateQueries({
          queryKey: parentQueryKeys.summary(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}

export function useUnarchiveParentMutation() {
  const queryClient = useQueryClient();

  return useUnarchiveParent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável desarquivado com sucesso");

        queryClient.invalidateQueries({ queryKey: parentQueryKeys.lists() });
        queryClient.invalidateQueries({
          queryKey: parentQueryKeys.detail(variables.parentId),
        });
        queryClient.invalidateQueries({
          queryKey: parentQueryKeys.summary(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}
