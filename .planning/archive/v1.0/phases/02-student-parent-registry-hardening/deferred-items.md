## Deferred Items

- 2026-04-19 — `client/src/components/ui/text-input.tsx:43` has a pre-existing TypeScript syntax error (`{errors.}`), which blocks `npm run build` but is unrelated to plan 02-02 codegen changes.
- 2026-04-19 — `client/node_modules/react-router/dist/development/index.d.mts:268` triggers `TS1003: Identifier expected` during `npm run build`; treated as an existing dependency/tooling issue outside this plan's changed files.
