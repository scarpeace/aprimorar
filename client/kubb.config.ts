import { defineConfig } from '@kubb/core'
import { pluginOas } from '@kubb/plugin-oas'
import { pluginTs } from '@kubb/plugin-ts'
import { pluginZod } from '@kubb/plugin-zod'
import { pluginReactQuery } from '@kubb/plugin-react-query'

export default defineConfig({
  input: {
    path: 'http://localhost:8080/v3/api-docs',
  },
  output: {
    path: './src/gen',
    clean: true,
    format: 'auto',
    lint: 'eslint',
  },

  plugins: [
    pluginOas({ generators: [] }),

    pluginTs({
      output: { path: './types' },
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
    }),

    pluginZod({
      output: { path: './schemas' },
      version: '4',
      dateType: 'string',
      coercion: true,
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
    }),

    pluginReactQuery({
      //Quando for implementar a autenticação te que tirar essa BaseURl daqui.
      client: { baseURL: 'http://localhost:8080' },
      output: { path: './hooks' },
      paramsType: 'inline',
      pathParamsType: 'inline',
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
      parser: 'zod',
      suspense: false,
    }),
  ],
})
