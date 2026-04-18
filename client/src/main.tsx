import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { QueryClientProvider } from "@tanstack/react-query";

import "@/lib/shared/api";
import { queryClient } from "@/lib/shared/queryClient.ts";

import "./index.css";
import App from "./App.tsx";
import "@/lib/validations/zod.ts";
import "react-datepicker/dist/react-datepicker.css";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </StrictMode>,
);
