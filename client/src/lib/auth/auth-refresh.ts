import "client-only";

import kubbFetchClient from "@kubb/plugin-client/clients/fetch";
import type { RefreshAccessTokenMutationResponse } from "@/lib/api/generated/types/RefreshAccessToken";
import {
  clearAccessToken,
  setAccessToken,
} from "./token-store";

async function refreshAccessToken() {
  try {
    const response = await kubbFetchClient<RefreshAccessTokenMutationResponse>({
      method: "POST",
      url: "/auth/refresh",
      credentials: "include",
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
