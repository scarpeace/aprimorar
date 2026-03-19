import axios from "axios"
import { ZodError } from "zod"

type ApiErrorResponse = {
  status?: number
  error?: string
  code?: string
  message?: string
  path?: string
  timestamp?: string
}

function getApiErrorMessage(data: unknown) {
  if (!data || typeof data !== "object") {
    return null
  }

  const apiError = data as ApiErrorResponse
  return typeof apiError.message === "string" && apiError.message.trim() ? apiError.message : null
}

export function getFriendlyErrorMessage(error: unknown) {
  if (!error) return ""

  if (error instanceof ZodError) {
    console.error("Schema validation error:", error.issues)
    return "Resposta da API em formato inesperado. Atualize a página ou contate o suporte."
  }

  if (axios.isAxiosError(error)) {
    const status = error.response?.status
    const apiMessage = getApiErrorMessage(error.response?.data)

    if (!status) {
      return "Não foi possível conectar ao servidor. Verifique se a API está rodando e tente novamente."
    }

    if (status === 400) return apiMessage ?? "Dados inválidos. Revise as informações e tente novamente."
    if (status === 404) return apiMessage ?? "Não encontramos o recurso solicitado."
    if (status === 409) return apiMessage ?? "Conflito de dados. Verifique se já existe um registro com essas informações."
    if (status >= 500) return "Erro no servidor. Tente novamente em instantes."

    return "Não foi possível concluir a solicitação. Tente novamente."
  }

  return "Ocorreu um erro inesperado. Tente novamente."
}
