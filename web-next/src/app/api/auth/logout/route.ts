import { NextResponse } from "next/server";
import { AUTH_ROLE_COOKIE, AUTH_TOKEN_COOKIE, AUTH_USER_COOKIE } from "@/lib/auth/constants";

export async function POST() {
  const response = NextResponse.json({ ok: true });

  response.cookies.delete(AUTH_TOKEN_COOKIE);
  response.cookies.delete(AUTH_ROLE_COOKIE);
  response.cookies.delete(AUTH_USER_COOKIE);

  return response;
}

