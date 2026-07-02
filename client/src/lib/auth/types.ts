export type UserRole = "ALUNO" | "COLABORADOR" | "RESPONSAVEL" | "ADMIN" | "SISTEMA";

export interface AuthUser {
  id: string;
  username: string;
  role: UserRole;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface AuthResponse {
  accessToken: string;
  expiresIn: number;
  user: AuthUser;
}

export interface AuthRequest {
  username: string;
  password: string;
}

export interface AuthSession {
  accessToken: string;
  user: AuthUser;
}

