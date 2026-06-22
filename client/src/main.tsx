import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { QueryClientProvider } from "@tanstack/react-query";

import "@/services/api.ts";

import "./style/index.css";
import App from "./App.tsx";
import "react-datepicker/dist/react-datepicker.css";
import { AuthProvider } from "@/auth/auth-context";
import { queryClient } from "./services/queryClient.ts";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <App />
      </AuthProvider>
    </QueryClientProvider>
  </StrictMode>,
);
