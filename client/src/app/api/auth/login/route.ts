import { NextResponse } from "next/server";
import { AUTH_ROLE_COOKIE, AUTH_TOKEN_COOKIE, AUTH_USER_COOKIE } from "@/lib/auth/constants";
import type { AuthRequest, AuthResponse } from "@/lib/auth/types";
import { getApiUrl } from "@/lib/api/server";

function getCookieOptions(maxAge: number) {
  return {
    httpOnly: true,
    maxAge,
    path: "/",
    sameSite: "lax" as const,
    secure: process.env.NODE_ENV === "production",
  };
}

async function getErrorMessage(response: Response) {
  try {
    const body = (await response.json()) as {
      message?: string;
      mensagens?: string[];
      error?: string;
    };

    return body.message ?? body.mensagens?.[0] ?? body.error ?? "Falha ao autenticar.";
  } catch {
    return "Falha ao autenticar.";
  }
}

export async function POST(request: Request) {
  let payload: AuthRequest;

  try {
    payload = (await request.json()) as AuthRequest;
  } catch {
    return NextResponse.json({ message: "Payload inválido." }, { status: 400 });
  }

  const backendResponse = await fetch(getApiUrl("/v1/auth/login"), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
    cache: "no-store",
  });

  if (!backendResponse.ok) {
    return NextResponse.json({ message: await getErrorMessage(backendResponse) }, { status: backendResponse.status });
  }

  const auth = (await backendResponse.json()) as AuthResponse;
  const response = NextResponse.json({ user: auth.user });
  const cookieOptions = getCookieOptions(auth.expiresIn);

  response.cookies.set(AUTH_TOKEN_COOKIE, auth.accessToken, cookieOptions);
  response.cookies.set(AUTH_ROLE_COOKIE, auth.user.role, cookieOptions);
  response.cookies.set(AUTH_USER_COOKIE, encodeURIComponent(JSON.stringify(auth.user)), cookieOptions);

  return response;
}

