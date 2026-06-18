import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { QueryClientProvider } from "@tanstack/react-query";

import "@/lib/shared/api/api.ts";

import "./index.css";
import App from "./App.tsx";
import "react-datepicker/dist/react-datepicker.css";
import { AuthProvider } from "@/features/auth/lib/authProvider.tsx";
import { queryClient } from "./lib/shared/api/queryClient.ts";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <App />
      </AuthProvider>
    </QueryClientProvider>
  </StrictMode>,
);
