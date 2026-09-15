import "client-only";

import kubbFetchClient from "@kubb/plugin-client/clients/fetch";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";
import { login as loginRequest } from "@/lib/api/generated/hooks/autenticação/useLogin";
import { logout as logoutRequest } from "@/lib/api/generated/hooks/autenticação/useLogout";
import type { LoginResponse } from "@/lib/api/generated/types/LoginResponse";
import { refreshOnce } from "./auth-refresh";
import {
  clearAccessToken,
  setAccessToken,
} from "./token-store";

function requireAccessToken(data: LoginResponse) {
  if (!data.accessToken) {
    throw new Error("Resposta de autenticação sem access token.");
  }

  return data.accessToken;
}

export async function login(email: string, password: string) {
  try {
    const data = await loginRequest(
      { email, password },
      { client: kubbFetchClient },
    );
    const accessToken = requireAccessToken(data);

    setAccessToken(accessToken);

    return accessToken;
  } catch (error) {
    throw new Error(getFriendlyErrorMessage(error));
  }
}

export async function refreshAccessToken() {
  return refreshOnce();
}


export async function logout() {
  try {
    await logoutRequest({ client: kubbFetchClient });
  } finally {
    clearAccessToken();
  }
}
