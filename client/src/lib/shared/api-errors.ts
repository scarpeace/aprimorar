import axios from "axios";
import { ZodError } from "zod";

export function getFriendlyErrorMessage(error: unknown): string {
  if (!error) return "";
//TODO implementar o logging mais pra frente

  if (error instanceof ZodError) {
    console.error("ZOD: Zod não conseguiu parsear a resposta da API", error.message);
    return "ZOD : Resposta da API em formato inesperado";
  }

  if (axios.isAxiosError(error)) {
    console.error("SERVER: Erro HTTP do servidor: ", error.message);
    const status = error.response?.status;
    const apiMessage =
      error.response?.data?.detail ?? error.response?.data?.message;

    if (status === 400)
      return (
        apiMessage ??
        "400 - Dados inválidos no corpo da requisição, por favor verifique os dados e tente novamente"
      );
    if (status === 404)
      return (
        apiMessage ??
        "404 - O recurso que você procura não foi encontrado, por favor verifique a URL e tente novamente"
      );
    if (status === 409)
      return (
        apiMessage ??
        "409 - Houve conflito de dados no sistema, por favor verifique os dados e tente novamente"
      );
  }

  return "Erro inesperado, contate o suporte";
}
