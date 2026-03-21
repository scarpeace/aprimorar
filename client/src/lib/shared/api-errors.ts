import axios from "axios";

export function getFriendlyErrorMessage(error: unknown): string {
  if (!error) return "";

  if (axios.isAxiosError(error)) {
    const status = error.response?.status;
    const apiMessage = error.response?.data?.message;

    if (status === 400) return apiMessage ?? "Dados inválidos";
    if (status === 404) return apiMessage ?? "Não encontrado";
    if (status === 409) return apiMessage ?? "Conflito de dados";
  }

  console.info("Algo deu errado ao carregar a página")
  console.error(error);
  return "Erro inesperado";
}
