import {
  findColaboradorByIdQueryKey,
  getAtendimentosQueryKey,
  getColaboradoresListQueryKey,
  getColaboradoresQueryKey,
  useArquivarColaborador,
  useCreateColaborador,
  useDeleteColaborador,
  useDesarquivarColaborador,
  useUpdateColaborador,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api/api";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useColaboradorMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const invalidateColaboradores = () => {
    queryClient.invalidateQueries({ queryKey: getColaboradoresQueryKey() });
    queryClient.invalidateQueries({ queryKey: getColaboradoresListQueryKey() });
  };

  const invalidateColaboradorDetail = (colaboradorId: string) => {
    queryClient.invalidateQueries({ queryKey: findColaboradorByIdQueryKey(colaboradorId) });
    queryClient.invalidateQueries({ queryKey: getAtendimentosQueryKey({ colaboradorId }) });
  };

  const createColaborador = useCreateColaborador({
    mutation: {
      onSuccess: (createdColaborador) => {
        toast.success("Colaborador criado com sucesso");
        invalidateColaboradores();
        navigate(`/colaboradores/${createdColaborador.id}`);
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao criar o colaborador",
        );
      },
    },
  });

  const updateColaborador = useUpdateColaborador({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador atualizado com sucesso");
        invalidateColaboradores();
        invalidateColaboradorDetail(variables.colaboradorId);
        navigate(`/colaboradores/${variables.colaboradorId}`);
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao atualizar o colaborador",
        );
      },
    },
  });

  const archiveColaborador = useArquivarColaborador({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador arquivado com sucesso");
        invalidateColaboradores();
        invalidateColaboradorDetail(variables.colaboradorId);
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao arquivar o colaborador",
        );
      },
    },
  });

  const unarchiveColaborador = useDesarquivarColaborador({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador desarquivado com sucesso");
        invalidateColaboradores();
        invalidateColaboradorDetail(variables.colaboradorId);
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao desarquivar o colaborador",
        );
      },
    },
  });

  const deleteColaborador = useDeleteColaborador({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador excluído com sucesso");
        invalidateColaboradores();
        invalidateColaboradorDetail(variables.colaboradorId);
        navigate("/colaboradores");
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao excluir o colaborador",
        );
      },
    },
  });

  return {
    createColaborador,
    updateColaborador,
    archiveColaborador,
    unarchiveColaborador,
    deleteColaborador,
  };
}
