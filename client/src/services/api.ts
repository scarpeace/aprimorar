import { axiosInstance } from "@kubb/plugin-client/clients/axios";
import axios from "axios";
import type { InternalAxiosRequestConfig } from "axios";
import { toast } from "sonner";
import {ZodError} from "zod";

const ACCESS_TOKEN_KEY = 'aprimorar.auth';
const apiBaseUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";

const apiConfig = {
  baseURL: apiBaseUrl,
  headers: { "Content-Type": "application/json" },
} as const;

export const api = axios.create(apiConfig);

// In-memory access token storage
export function setStoredAccessToken(token: string | null): void {
  localStorage.setItem(ACCESS_TOKEN_KEY, token ?? '');
}

export function getStoredAccessToken(): string | null {
  return localStorage.getItem(ACCESS_TOKEN_KEY);
}

export function clearStoredAccessToken(): void {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
}

// Inject access token into every private request
api.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
      if (!isLoginUrl(config.url) && getStoredAccessToken()) {
        config.headers.Authorization = `Bearer ${getStoredAccessToken()}`;
      }

      return config;
    },
    (error) => Promise.reject(error)
);

// Intercept 401 errors to automatically refresh using localStorage token
api.interceptors.response.use(
    (response) => response,
    async (error) => {
      if (!axios.isAxiosError(error)) {
        return Promise.reject(error);
      }

      if (isLoginUrl(error.config?.url)) {
        return Promise.reject(error);
      }

      if (error.response?.status === 401) {
        clearStoredAccessToken();
        setStoredAccessToken(null);
        toast.error("Sessão expirada. Faça login novamente.");
        window.location.href = "/login";
      }

      return Promise.reject(error)
    }
);

Object.assign(axiosInstance.defaults, apiConfig);

function isLoginUrl(url?: string): boolean {
  if (!url) {
    return false;
  }
  return url === "/v1/auth/login" || url.endsWith("/v1/auth/login");
}

export function getFriendlyErrorMessage(error: any): string {
  if (axios.isAxiosError(error)) {
    return error.response?.data?.message || error.message;
  }

 if (error instanceof ZodError) {
     return "Resposta da API em formato inesperado";
 }

  return error.message || "Ocorreu um erro desconhecido.";
}
