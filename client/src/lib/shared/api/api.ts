import { axiosInstance } from "@kubb/plugin-client/clients/axios";
import axios from "axios";

import { attachAuthInterceptors } from "./interceptors";

const apiBaseUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";

const sharedApiConfig = {
  baseURL: apiBaseUrl,
  headers: { "Content-Type": "application/json" },
} as const;

export const api = axios.create(sharedApiConfig);

Object.assign(axiosInstance.defaults, sharedApiConfig);


attachAuthInterceptors(api);
attachAuthInterceptors(axiosInstance);

export { getFriendlyErrorMessage } from "./error-message";
