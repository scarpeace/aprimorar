import axios from "axios";
import { ZodError } from "zod";

import type { ProblemResponse } from "./problem-response";

function extractProblemMessage(data: unknown): string | undefined {
  if (!data || typeof data !== "object") {
    return undefined;
  }

  const problem = data as ProblemResponse;
  return problem.message ?? problem.detail ?? problem.title;
}

export function getFriendlyErrorMessage(error: unknown): string {
  if (!error) return "";

  if (error instanceof ZodError) {
    return "Resposta da API em formato inesperado";
  }

  if (axios.isAxiosError(error)) {
    const apiMessage = extractProblemMessage(error.response?.data);
    return apiMessage ?? error.message ?? "Erro fora de escopo, contate o suporte";
  }

  return "Erro não reconhecido! Contate o suporte imediatamente";
}
