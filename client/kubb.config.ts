import { defineConfig } from "@kubb/core";
import { pluginOas } from "@kubb/plugin-oas";
import { pluginReactQuery } from "@kubb/plugin-react-query";
import { pluginTs } from "@kubb/plugin-ts";
import { pluginZod } from "@kubb/plugin-zod";

export default defineConfig({
  input: {
    //TODO: tem que mover isso aqui pra uma variável de ambiente
    path: "http://localhost:8080/v3/api-docs",
  },
  output: {
    path: "./src/lib/api/generated",
    clean: true,
    format: "prettier",
  },
  plugins: [
    pluginOas(),
    pluginTs(),
    pluginZod({
      output: { path: "./zod", barrelType: "named" },
      version: "4",
      dateType: "string",
      coercion: false,
      operations: false,
    }),
    pluginReactQuery({
      output: { path: "./hooks", barrelType: "named" },
      paramsType: "inline",
      pathParamsType: "inline",
      group: {
        type: "tag",
        name: ({ group }) => group.toLowerCase(),
      },
      client: {
        importPath: "@/lib/backend/client",
      },
      suspense: false,
      paramsCasing: "camelcase",
    }),
  ],
});
