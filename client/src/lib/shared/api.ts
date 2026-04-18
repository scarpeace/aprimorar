import { axiosInstance } from "@kubb/plugin-client/clients/axios";
import axios from "axios";
import { ZodError } from "zod";

const apiBaseUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";

const sharedApiConfig = {
  baseURL: apiBaseUrl,
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
  withXSRFToken: true,
  xsrfCookieName: "XSRF-TOKEN",
  xsrfHeaderName: "X-XSRF-TOKEN",
} as const;

export const api = axios.create(sharedApiConfig);

Object.assign(axiosInstance.defaults, sharedApiConfig);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error(getFriendlyErrorMessage(error));
    return Promise.reject(error);
  },
);

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
