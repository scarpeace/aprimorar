import fetch, {
  type Client,
  type RequestConfig,
  type ResponseConfig,
  type ResponseErrorConfig,
} from "@kubb/plugin-client/clients/fetch";

fetch.setConfig({
  baseURL: process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080",
  credentials: "include",
});

export type {
  Client,
  RequestConfig,
  ResponseConfig,
  ResponseErrorConfig,
};

export default fetch as Client;
