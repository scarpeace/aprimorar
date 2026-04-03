import { defineConfig } from '@kubb/core'
import { pluginOas } from '@kubb/plugin-oas'
import { pluginTs } from '@kubb/plugin-ts'
import { pluginFaker } from '@kubb/plugin-faker'
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
      output: { path: './types'},
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
    }),

    pluginZod({
      output: { path: './zod' },
      // inferred: true,
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
    }),

    pluginReactQuery({
      //Quando for implementar a autenticação te que tirar essa BaseURl daqui.
      client: { baseURL: 'http://localhost:8080' },
      output: { path: './hooks', barrelType: 'named' },
      paramsType: 'inline',
      // parser: 'zod',
      pathParamsType: 'inline',
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
      suspense: false,
      paramsCasing: 'camelcase',
    }),

   // pluginFaker({
   //    output: {
   //      path: './mocks',
   //      banner: '/* eslint-disable no-alert, no-console */',
   //      barrelType: 'named',
   //      footer: ''
   //    },
   //    group: {
   //      type: 'tag',
   //      name: ({ group }) => `${group}Mocks`,
   //    },
   //   dateType: 'string',
   //    // dateParser: 'dayjs',
   //    unknownType: 'unknown',
   //    regexGenerator: 'faker',
   //    seed: [100],
   //  }),


  ],
})
