//Importante salientar que o client está dessa maneira porque o KUBB exige esse formato para funcionar corretamente;

import type { ErrorResponse } from "@/lib/api/generated/types/ErrorResponse";

const API_PROXY_PREFIX = "/api/proxy";

export type RequestConfig<TData = unknown> = {
  url?: string;
  method: "GET" | "PUT" | "PATCH" | "POST" | "DELETE";
  params?: object;
  data?: TData | FormData;
  responseType?: "arraybuffer" | "blob" | "document" | "json" | "text" | "stream";
  signal?: AbortSignal;
  headers?: HeadersInit;
};

export type ResponseConfig<TData = unknown> = {
  data: TData;
  status: number;
  statusText: string;
};

export type ResponseErrorConfig<TError = unknown> = TError;

type ApiErrorPayload = ErrorResponse & {
  message?: string;
};

// eslint-disable-next-line @typescript-eslint/no-unused-vars
export type Client = <TData, _TError = unknown, TVariables = unknown>(
  config: RequestConfig<TVariables>,
) => Promise<ResponseConfig<TData>>;

function buildUrl(url = "", params?: object) {
  const searchParams = new URLSearchParams();

  Object.entries(params ?? {}).forEach(([key, value]) => {
    if (value === undefined || value === null || value === "") {
      return;
    }

    if (Array.isArray(value)) {
      value.forEach((item) => searchParams.append(key, String(item)));
      return;
    }

    searchParams.append(key, String(value));
  });

  const query = searchParams.toString();
  return `${API_PROXY_PREFIX}${url}${query ? `?${query}` : ""}`;
}

async function parseResponse<TData>(response: Response, responseType?: RequestConfig["responseType"]) {
  if (response.status === 204) {
    return undefined as TData;
  }

  if (responseType === "blob") {
    return (await response.blob()) as TData;
  }

  const contentType = response.headers.get("content-type") ?? "";

  if (contentType.includes("json")) {
    return (await response.json()) as TData;
  }

  return (await response.text()) as TData;
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
export const client: Client = async <TData, _TError = unknown, TVariables = unknown>(config: RequestConfig<TVariables>) => {
  const headers = new Headers(config.headers);

  if (!(config.data instanceof FormData) && !headers.has("content-type")) {
    headers.set("content-type", "application/json");
  }

  const response = await fetch(buildUrl(config.url, config.params), {
    method: config.method,
    body: config.data instanceof FormData ? config.data : config.data === undefined ? undefined : JSON.stringify(config.data),
    headers,
    signal: config.signal,
    credentials: "same-origin",
  });

  const data = await parseResponse<TData>(response, config.responseType);

  if (response.status === 401 && typeof window !== "undefined") {
    window.location.href = "/login";
  }

  if (!response.ok) {
    throw data || new Error(response.statusText);
  }

  return {
    data,
    status: response.status,
    statusText: response.statusText,
  };
};

export default client;

export function getFriendlyErrorMessage(error: unknown): string {
  if (error instanceof Error) {
    return error.message;
  }

  if (typeof error === "string") {
    return error;
  }

  if (error && typeof error === "object") {
    const payload = error as ApiErrorPayload;
    return payload.mensagens?.[0] ?? payload.message ?? payload.erro ?? "Ocorreu um erro desconhecido.";
  }

  return "Ocorreu um erro desconhecido.";
}
