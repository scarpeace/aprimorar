import axios from "axios";
import { ZodError } from "zod";

export function getFriendlyErrorMessage(error: unknown): string {
  if (!error) return "";

  if (error instanceof ZodError) {
    return "Resposta da API em formato inesperado"
  }

  if (axios.isAxiosError(error)) {
    const status = error.response?.status;
    const apiMessage = error.response?.data?.detail ?? error.response?.data?.message

    if (status === 400) return apiMessage ?? "400 - Dados inválidos no corpo da requisição, por favor verifique os dados e tente novamente";
    if (status === 404) return apiMessage ?? "404 - O recurso que você procura não foi encontrado, por favor verifique a URL e tente novamente";
    if (status === 409) return apiMessage ?? "409 - Houve conflito de dados no sistema, por favor verifique os dados e tente novamente";
  }

  //TODO implementar o logging mais pra frente
  console.info("Algo deu errado ao carregar a página")
  console.error(error);
  return "Erro inesperado";
}
