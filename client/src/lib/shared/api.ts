import { axiosInstance } from "@kubb/plugin-client/clients/axios";
import axios from "axios";
import { ZodError } from "zod";
import { keepPreviousData, QueryClient } from "@tanstack/react-query";
import { AUTH_STORAGE_KEY, readStoredAuth } from "@/features/auth/lib/auth-context";
import { toast } from "sonner";

const apiBaseUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";

const sharedApiConfig = {
  baseURL: apiBaseUrl,
  headers: { "Content-Type": "application/json" },
} as const;

export const api = axios.create(sharedApiConfig);
Object.assign(axiosInstance.defaults, sharedApiConfig);

// Rota de login não precisa de token de autenticação
function isLoginUrl(url?: string): boolean {
  if (!url) {
    return false;
  }

  return url === "/v1/auth/login" || url.endsWith("/v1/auth/login");
}

function authRequestInterceptor(config: Parameters<NonNullable<Parameters<typeof api.interceptors.request.use>[0]>>[0]) {
  const stored = readStoredAuth();
  if (!isLoginUrl(config.url) && stored?.token) {
    config.headers.Authorization = `Bearer ${stored.token}`;
  }
  return config;
}

function authResponseInterceptor(error: unknown) {
  if (!axios.isAxiosError(error)) {
    console.error(getFriendlyErrorMessage(error));
    return Promise.reject(error);
  }

  if (isLoginUrl(error.config?.url)) {
    return Promise.reject(error);
  }

  if (error.response?.status === 401) {
    localStorage.removeItem(AUTH_STORAGE_KEY);
    toast.error("Sessão expirada. Faça login novamente.");
    window.location.href = "/login";
  }

  console.error(getFriendlyErrorMessage(error));
  return Promise.reject(error);
}

api.interceptors.request.use(authRequestInterceptor, (error) => Promise.reject(error));
api.interceptors.response.use((response) => response, authResponseInterceptor);

axiosInstance.interceptors.request.use(authRequestInterceptor, (error) => Promise.reject(error));
axiosInstance.interceptors.response.use((response) => response, authResponseInterceptor);

export function getFriendlyErrorMessage(error: unknown): string {
  if (!error) return "";

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

// TODO: Esse query client é necessário ou pode ser removido? como mudar a configuração do kubb?
export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 5,
      retry: 1,
      placeholderData: keepPreviousData,
      refetchOnWindowFocus: false,
    },
  },
});
