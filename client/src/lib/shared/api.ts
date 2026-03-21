import axios from "axios";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("API Error:", error.response?.data || error.message);
    return Promise.reject(error);
  }
);

export function getFriendlyErrorMessage(error: unknown): string {
  if (!error) return "";

  if (axios.isAxiosError(error)) {
    const status = error.response?.status;
    const apiMessage = error.response?.data?.message;

    if (status === 400) return apiMessage ?? "Dados inválidos";
    if (status === 404) return apiMessage ?? "Não encontrado";
    if (status === 409) return apiMessage ?? "Conflito de dados";
  }

  return "Erro inesperado";
}
