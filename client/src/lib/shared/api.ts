import { axiosInstance } from "@kubb/plugin-client/clients/axios";

import { api } from "./api/http-client";
import { attachAuthInterceptors } from "./api/interceptors";

attachAuthInterceptors(api);
attachAuthInterceptors(axiosInstance);

export { api };
export { getFriendlyErrorMessage } from "./api/error-message";
