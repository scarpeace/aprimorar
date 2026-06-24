import { createContext, useCallback, useContext, useState, type ReactNode } from "react";
import {type AuthRequestDTO, type AuthResponseDTO, useLogin, type UserResponseDTO} from "@/kubb";
import {toast} from "sonner";
import {getFriendlyErrorMessage} from "@/services/api.ts";

interface AuthState {
    accessToken: string | null;
    user: UserResponseDTO | null;
    isAuthenticated: boolean;
}

interface AuthContextValue extends AuthState {
    login: (request: AuthRequestDTO) => void;
    logout: () => void;
    isLoginPending: boolean;
}

const AuthContext = createContext<AuthContextValue | null>(null);

const TOKEN_KEY = "access_token";
const USER_KEY = "auth_user";

export function AuthProvider({ children }: { children: ReactNode }) {
    const [state, setState] = useState<AuthState>(() => {
        // Rehydrata do localStorage no boot
        const token = localStorage.getItem(TOKEN_KEY);
        const raw = localStorage.getItem(USER_KEY);
        const user: UserResponseDTO | null = raw ? JSON.parse(raw) : null;

        return {
            accessToken: token,
            user,
            isAuthenticated: !!token && !!user,
        };
    });

    const loginMutation = useLogin({
        mutation:{
            onError: (error: Error) => {
                toast.error(getFriendlyErrorMessage(error));
            },
            onSuccess: (response: AuthResponseDTO) => {
              localStorage.setItem(TOKEN_KEY, response.accessToken);
              localStorage.setItem(USER_KEY, JSON.stringify(response.user));
              setState({ accessToken: response.accessToken, user: response.user, isAuthenticated: true })
            },
        }
    });

    const login = useCallback((request: AuthRequestDTO) => {
        loginMutation.mutate({ data: request });
    }, [loginMutation]);

    const logout = useCallback(() => {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
        setState({ accessToken: null, user: null, isAuthenticated: false });
    }, []);

    return (
        <AuthContext.Provider value={{ ...state, login, logout, isLoginPending: loginMutation.isPending }}>
            {children}
        </AuthContext.Provider>
    );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useAuth must be used inside <AuthProvider>");
    return ctx;
}
