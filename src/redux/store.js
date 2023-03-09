import { configureStore } from '@reduxjs/toolkit';
import  blogSlice  from './features/blogSlice';
// import AuthProvider from '../hooks/useFirebase';

export const store = configureStore({
  reducer: {
    blogs:blogSlice,
    // auth:AuthProvider(),
  },
})