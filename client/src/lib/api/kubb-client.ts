import kubbFetchClient, {
  type Client,
  type RequestConfig,
  type ResponseConfig,
  type ResponseErrorConfig,
} from "@kubb/plugin-client/clients/fetch";
import type { RefreshAccessTokenMutationResponse } from "@/lib/api/generated/types/RefreshAccessToken";
import {
  clearAccessToken,
  getAccessToken,
  setAccessToken,
} from "@/auth/token-store";

kubbFetchClient.setConfig({
  baseURL: process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080",
  credentials: "include",
  headers: {
    "Content-Type": "application/json",
  },
});

function getHeaders(headers: RequestConfig["headers"]) {
  return Array.isArray(headers) ? Object.fromEntries(headers) : headers ?? {};
}

export const publicClient: Client = async <TData, TError = unknown, TVariables = unknown>(
  config: RequestConfig<TVariables>,
) => {
  const response = await kubbFetchClient<TData, TError, TVariables>({
    ...config,
    credentials: "include",
  });

  if (response.status < 200 || response.status >= 300) {
    throw response.data;
  }

  return response;
};

async function refreshAccessToken() {
  try {
    const response = await publicClient<RefreshAccessTokenMutationResponse>({
      method: "POST",
      url: "/auth/refresh",
    });

    const accessToken = response.data.accessToken;

    if (!accessToken) {
      clearAccessToken();
      return null;
    }

    setAccessToken(accessToken);
    return accessToken;
  } catch {
    clearAccessToken();
    return null;
  }
}

let refreshPromise: Promise<string | null> | null = null;

export function refreshOnce() {
  if (!refreshPromise) {
    refreshPromise = refreshAccessToken().finally(() => {
      refreshPromise = null;
    });
  }

  return refreshPromise;
}

async function request<TData, TError, TVariables>(
  config: RequestConfig<TVariables>,
  token: string,
): Promise<ResponseConfig<TData>> {
  return kubbFetchClient<TData, TError, TVariables>({
    ...config,
    credentials: "include",
    headers: {
      ...getHeaders(config.headers),
      Authorization: `Bearer ${token}`,
    },
  });
}

export const client: Client = async <TData, TError = unknown, TVariables = unknown>(
  config: RequestConfig<TVariables>,
) => {
  let token = getAccessToken();

  if (!token) {
    token = await refreshOnce();
  }

  if (!token) {
    throw new Error("Usuário não autenticado.");
  }

  let response = await request<TData, TError, TVariables>(config, token);

  if (response.status === 401) {
    const refreshedToken = await refreshOnce();

    if (refreshedToken) {
      response = await request<TData, TError, TVariables>(config, refreshedToken);
    }
  }

  if (response.status < 200 || response.status >= 300) {
    throw response.data;
  }

  return response;
};

export type {
  Client,
  RequestConfig,
  ResponseConfig,
  ResponseErrorConfig,
};

export default client;
