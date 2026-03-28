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
    path: './src/kubb',
    clean: true,
    format: 'auto',
  },

  plugins: [
    pluginOas({ generators: [] , collisionDetection: true}),
    pluginTs({
      output: { path: './types' },
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
    }),

    // pluginFaker({
    //   output: { path: './faker' },
    //   group: {
    //     type: 'tag',
    //     name: ({ group }) => group.toLowerCase(),
    //   },
    // }),

    pluginZod({
      output: { path: './schemas' },
      version: '4',
      inferred: true,
      importPath: '@/lib/validations/zod',
      dateType: 'string',
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
      parser: 'zod',
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
      suspense: false,
    }),
  ],
})
function pluginFaker(arg0: { output: { path: string }; group: { type: string; name: ({ group }: { group: any }) => any } }) {
  throw new Error('Function not implemented.')
}
