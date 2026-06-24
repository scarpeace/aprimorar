import { Navigate, Outlet, useLocation } from "react-router-dom";
import {useAuth} from "@/auth/authContext.tsx";

interface ProtectedRouteProps {
  /** Roles que têm acesso. Se vazio, qualquer usuário autenticado passa. */
  allowedRoles?: string[];
}

export function ProtectedRoute({ allowedRoles = [] }: Readonly<ProtectedRouteProps>) {
  const { isAuthenticated, user } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    // Salva a rota pretendida para redirecionar depois do login
    sessionStorage.setItem("redirectAfterLogin", location.pathname + location.search);
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles.length > 0) {
    const hasRole = user?.role
    if (!hasRole) {
      return <Navigate to="/forbidden" replace />;
    }
  }

  return <Outlet />;
}

