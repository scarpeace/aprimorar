import { useLogin } from "@/kubb";

export function useAuthMutations() {
  const loginMutation = useLogin();

  return {
    loginMutation,
  };
}
