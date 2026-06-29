"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";
import { resolveRedirectPath } from "@/lib/auth/redirects";

export function LoginForm() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [isPending, setIsPending] = useState(false);

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);
    setIsPending(true);

    try {
      const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        const body = (await response.json().catch(() => null)) as { message?: string } | null;
        setError(body?.message ?? "Falha ao autenticar.");
        return;
      }

      router.replace(resolveRedirectPath(searchParams.get("redirect")));
      router.refresh();
    } catch {
      setError("Não foi possível conectar ao servidor.");
    } finally {
      setIsPending(false);
    }
  }

  return (
    <form className="flex flex-col gap-3" onSubmit={handleSubmit}>
      <label className="form-control w-full">
        <span className="label-text mb-1 font-semibold text-base-content/75">Email</span>
        <input
          aria-label="Email"
          autoComplete="username"
          className="input input-bordered w-full bg-base-100"
          onChange={(event) => setUsername(event.target.value)}
          placeholder="voce@aprimorar.com"
          required
          type="email"
          value={username}
        />
      </label>

      <label className="form-control w-full">
        <span className="label-text mb-1 font-semibold text-base-content/75">Senha</span>
        <input
          aria-label="Senha"
          autoComplete="current-password"
          className="input input-bordered w-full bg-base-100"
          onChange={(event) => setPassword(event.target.value)}
          placeholder="Digite sua senha"
          required
          type="password"
          value={password}
        />
      </label>

      {error ? (
        <p className="rounded-lg border border-error/30 bg-error/10 px-3 py-2 text-sm text-error">{error}</p>
      ) : null}

      <button className="btn btn-success mt-3 w-full" disabled={isPending} type="submit">
        {isPending ? "Entrando..." : "Entrar"}
      </button>
    </form>
  );
}

