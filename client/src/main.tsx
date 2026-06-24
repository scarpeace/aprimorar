import "react-datepicker/dist/react-datepicker.css";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import {keepPreviousData, QueryClient, QueryClientProvider} from "@tanstack/react-query";

import "@/services/api.ts";
import "./style/index.css";
import App from "./App.tsx";
import { AuthProvider } from "@/auth/authContext.tsx";

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: 1000 * 60 * 5,
            retry: 1,
            placeholderData: keepPreviousData,
            refetchOnWindowFocus: false,
        },
    },
});

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <App />
      </AuthProvider>
    </QueryClientProvider>
  </StrictMode>,
);
