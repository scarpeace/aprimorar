const DEFAULT_API_URL = "http://localhost:8080";

export function getApiBaseUrl() {
  return process.env.API_URL ?? process.env.NEXT_PUBLIC_API_URL ?? DEFAULT_API_URL;
}

export function getApiUrl(path: string) {
  return `${getApiBaseUrl()}${path}`;
}

