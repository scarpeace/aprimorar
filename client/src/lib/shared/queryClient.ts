import { keepPreviousData, QueryClient, type Query } from "@tanstack/react-query";

const protectedQueryPrefixes = ["/v1/dashboard", "/v1/students", "/v1/parents", "/v1/employees", "/v1/events", "/v1/auth/me"];

function getQueryUrl(query: Query) {
  const [firstEntry] = query.queryKey;

  if (typeof firstEntry === "object" && firstEntry !== null && "url" in firstEntry) {
    const url = firstEntry.url;

    return typeof url === "string" ? url : null;
  }

  return null;
}

function isProtectedQuery(query: Query) {
  const url = getQueryUrl(query);

  return url !== null && protectedQueryPrefixes.some((prefix) => url.startsWith(prefix));
}

export async function clearProtectedQueryCache(queryClient: QueryClient) {
  await queryClient.cancelQueries({ predicate: isProtectedQuery });
  queryClient.removeQueries({ predicate: isProtectedQuery });
}

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 5,
      retry: 1,
      placeholderData: keepPreviousData,
      refetchOnWindowFocus: false,
    },
  },
});
