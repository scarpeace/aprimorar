import axios from "axios";
import type { AxiosInstance, InternalAxiosRequestConfig } from "axios";
import { toast } from "sonner";

import { clearStoredAuth, readStoredAuth } from "@/features/auth/lib/auth-storage";

function isLoginUrl(url?: string): boolean {
  if (!url) {
    return false;
  }

  return url === "/v1/auth/login" || url.endsWith("/v1/auth/login");
}

function authRequestInterceptor(config: InternalAxiosRequestConfig) {
  const stored = readStoredAuth();
  if (!isLoginUrl(config.url) && stored?.token) {
    config.headers.Authorization = `Bearer ${stored.token}`;
  }
  return config;
}

function authResponseInterceptor(error: unknown) {
  if (!axios.isAxiosError(error)) {
    return Promise.reject(error);
  }

  if (isLoginUrl(error.config?.url)) {
    return Promise.reject(error);
  }

  if (error.response?.status === 401) {
    clearStoredAuth();
    toast.error("Sessão expirada. Faça login novamente.");
    window.location.href = "/login";
  }

  return Promise.reject(error);
}

export function attachAuthInterceptors(instance: AxiosInstance) {
  instance.interceptors.request.use(authRequestInterceptor, (error) => Promise.reject(error));
  instance.interceptors.response.use((response) => response, authResponseInterceptor);
}
