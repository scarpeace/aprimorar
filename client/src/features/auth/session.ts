import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import { AUTH_TOKEN_COOKIE, AUTH_USER_COOKIE } from "./constants";
import type { AuthSession, AuthUser } from "./types";

function parseUser(value: string | undefined): AuthUser | null {
  if (!value) {
    return null;
  }

  try {
    return JSON.parse(decodeURIComponent(value)) as AuthUser;
  } catch {
    return null;
  }
}

export async function getSession(): Promise<AuthSession | null> {
  const cookieStore = await cookies();
  const accessToken = cookieStore.get(AUTH_TOKEN_COOKIE)?.value ?? null;
  const user = parseUser(cookieStore.get(AUTH_USER_COOKIE)?.value);

  if (!accessToken || !user) {
    return null;
  }

  return { accessToken, user };
}

export async function requireSession() {
  const session = await getSession();

  if (!session) {
    redirect("/login");
  }

  return session;
}

