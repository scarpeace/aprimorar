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
    const apiMessage = error.message;
    return apiMessage ?? "Erro fora de escopo, contate o suporte";
  }

  return "Erro não reconhecido! Contate o suporte imediatamente";
}
