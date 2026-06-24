import axios, { type AxiosRequestConfig } from "axios";
import { ZodError } from "zod";

const TOKEN_KEY = import.meta.env.VITE_TOKEN_KEY ?? "access_token";

export type RequestConfig<TData = unknown> = {
    url?: string;
    method: "GET" | "PUT" | "PATCH" | "POST" | "DELETE";
    params?: object;
    data?: TData | FormData;
    responseType?: "arraybuffer" | "blob" | "document" | "json" | "text" | "stream";
    signal?: AbortSignal;
    headers?: AxiosRequestConfig["headers"];
};

export type ResponseConfig<TData = unknown> = {
    data: TData;
    status: number;
    statusText: string;
};

export type ResponseErrorConfig<TError = unknown> = TError;

export type Client = <TData, _TError = unknown, TVariables = unknown>(
    config: RequestConfig<TVariables>,
) => Promise<ResponseConfig<TData>>;

export const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_URL ?? "http://localhost:8080",
    headers: { "Content-Type": "application/json" },
});

// Injeta o token em toda requisição
apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem(TOKEN_KEY);
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Trata 401 globalmente — redireciona pro login limpando o storage
apiClient.interceptors.response.use(
    (res) => res,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem(TOKEN_KEY);
            localStorage.removeItem("auth_user");
            // Força reload para o router pegar o estado limpo
            window.location.href = "/login";
        }
        return Promise.reject(error);
    }
);

export const client: Client = async (config) => {
    const response = await apiClient.request(config);

    return {
        data: response.data,
        status: response.status,
        statusText: response.statusText,
    };
};

export default client;

export function getFriendlyErrorMessage(error: unknown): string {
  if (axios.isAxiosError(error)) {
    return error.response?.data?.message || error.message;
  }

 if (error instanceof ZodError) {
     return "Resposta da API em formato inesperado";
 }

  if (error instanceof Error) {
      return error.message;
  }

  return "Ocorreu um erro desconhecido.";
}
