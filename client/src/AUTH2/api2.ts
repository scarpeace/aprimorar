// import axios, { type AxiosInstance, type InternalAxiosRequestConfig } from 'axios';
// import { RefreshResponse } from './types';
//
// const REFRESH_TOKEN_KEY = 'rt_auth_app';
//
// export const publicApi: AxiosInstance = axios.create({
//   baseURL: 'https://example.com',
//   headers: { 'Content-Type': 'application/json' },
// });
//
// export const privateApi: AxiosInstance = axios.create({
//   baseURL: 'https://example.com',
//   headers: { 'Content-Type': 'application/json' },
// });
//
// // In-memory access token storage
// let memoryToken: string | null = null;
//
// export const setAccessToken = (token: string | null): void => {
//   memoryToken = token;
// };
//
// export const getStoredRefreshToken = (): string | null => {
//   return localStorage.getItem(REFRESH_TOKEN_KEY);
// };
//
// export const setStoredRefreshToken = (token: string | null): void => {
//   if (token) {
//     localStorage.setItem(REFRESH_TOKEN_KEY, token);
//   } else {
//     localStorage.removeItem(REFRESH_TOKEN_KEY);
//   }
// };
//
// // Inject access token into every private request
// privateApi.interceptors.request.use(
//     (config: InternalAxiosRequestConfig) => {
//       if (memoryToken && !config.headers['Authorization']) {
//         config.headers['Authorization'] = `Bearer ${memoryToken}`;
//       }
//       return config;
//     },
//     (error) => Promise.reject(error)
// );
//
// // Intercept 401 errors to automatically refresh using localStorage token
// privateApi.interceptors.response.use(
//     (response) => response,
//     async (error) => {
//       const prevRequest = error?.config;
//
//       if (error?.response?.status === 401 && prevRequest && !prevRequest._retry) {
//         prevRequest._retry = true;
//         const currentRefreshToken = getStoredRefreshToken();
//
//         if (!currentRefreshToken) {
//           return Promise.reject(error);
//         }
//
//         try {
//           // Send refresh token in the body or header depending on backend requirements
//           const response = await publicApi.post<RefreshResponse>('/auth/refresh', {
//             refreshToken: currentRefreshToken,
//           });
//
//           const { accessToken, refreshToken: newRefreshToken } = response.data;
//
//           setAccessToken(accessToken);
//           setStoredRefreshToken(newRefreshToken); // Update storage with rotated token
//
//           prevRequest.headers['Authorization'] = `Bearer ${accessToken}`;
//           return privateApi(prevRequest);
//         } catch (refreshError) {
//           setAccessToken(null);
//           setStoredRefreshToken(null);
//           window.location.href = '/login';
//           return Promise.reject(refreshError);
//         }
//       }
//       return Promise.reject(error);
//     }
// );
