import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { publicApi, privateApi, setAccessToken, getStoredRefreshToken, setStoredRefreshToken } from './api';
import { User, AuthResponse, RefreshResponse, AuthContextType } from './types';

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const queryClient = useQueryClient();
    const [isInitializing, setIsInitializing] = useState<boolean>(true);

    // Profile Cache Query
    // const { data: user = null, isLoading: isUserLoading, refetch } = useQuery<User | null>({
    //     queryKey: ['authUser'],
    //     queryFn: async () => {
    //         const res = await privateApi.get<User>('/auth/me');
    //         return res.data;
    //     },
    //     enabled: false,
    //     retry: false,
    // });

    const { data: user = null, isLoading: isUserLoading, refetch } = useQuery<User | null>({
        queryKey: ['authUser'],
        queryFn: async () => {
            const res = await privateApi.get<User>('/auth/me');
            return res.data;
        },
        enabled: false,
        retry: false,
    });

    // Startup: Attempt token initialization from localStorage
    useEffect(() => {
        const initializeAuth = async () => {
            const savedRefreshToken = getStoredRefreshToken();

            if (!savedRefreshToken) {
                setIsInitializing(false);
                return;
            }

            try {
                const res = await publicApi.post<RefreshResponse>('/auth/refresh', {
                    refreshToken: savedRefreshToken,
                });
                setAccessToken(res.data.accessToken);
                setStoredRefreshToken(res.data.refreshToken);
                await refetch();
            } catch {
                setAccessToken(null);
                setStoredRefreshToken(null);
            } finally {
                setIsInitializing(false);
            }
        };

        initializeAuth();
    }, [refetch]);

    // Login Mutation
    const loginMutation = useMutation<AuthResponse, Error, Record<string, string>>({
        mutationFn: async (credentials) => {
            const res = await publicApi.post<AuthResponse>('/auth/login', credentials);
            return res.data;
        },
        onSuccess: (data) => {
            setAccessToken(data.accessToken);
            setStoredRefreshToken(data.refreshToken);
            queryClient.setQueryData(['authUser'], data.user);
        },
    });

    // Logout Mutation
    const logoutMutation = useMutation<void, Error, void>({
        mutationFn: async () => {
            const savedRefreshToken = getStoredRefreshToken();
            await privateApi.post('/auth/logout', { refreshToken: savedRefreshToken });
        },
        onSuccess: () => {
            setAccessToken(null);
            setStoredRefreshToken(null);
            queryClient.setQueryData(['authUser'], null);
            queryClient.clear();
        },
    });

    const isAuthenticated = !!user;

    return (
        <AuthContext.Provider
            value={{
                user,
                isAuthenticated,
                isLoading: isInitializing || isUserLoading,
                login: loginMutation.mutateAsync,
                logout: logoutMutation.mutate,
                isLoggingIn: loginMutation.isPending,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};