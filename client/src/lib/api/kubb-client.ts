
import kubbFetchClient, {
  type Client,
  type RequestConfig,
  type ResponseConfig,
  type ResponseErrorConfig,
} from "@kubb/plugin-client/clients/fetch";
import { refreshOnce } from "@/lib/auth/auth-refresh";
import { getAccessToken } from "@/lib/auth/token-store";


kubbFetchClient.setConfig({
  baseURL: process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080",
  credentials: "include",
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

    if (!refreshedToken) {
      return response;
    }

    response = await request<TData, TError, TVariables>(config, refreshedToken);
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
