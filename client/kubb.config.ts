import { defineConfig } from '@kubb/core'
import { pluginOas } from '@kubb/plugin-oas'
import { pluginTs } from '@kubb/plugin-ts'
import { pluginReactQuery } from '@kubb/plugin-react-query'

export default defineConfig({
  input: {
    path: 'http://localhost:8080/v3/api-docs',
  },
  output: {
    path: './src/kubb',
    clean: true,
    format: 'prettier',
  },

  plugins: [
    pluginOas(),
    pluginTs(),

    pluginReactQuery({
      output: { path: './hooks', barrelType: 'named' },
      paramsType: 'inline',
      pathParamsType: 'inline',
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
      client: {
        importPath: '@/services/api',
      },
      suspense: false,
      paramsCasing: 'camelcase',
      // mutation: { methods: ["post", "put", "patch", "delete"] },
      // query: { methods: ["get"] },
    }),
  ],
})
