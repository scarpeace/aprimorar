## Deferred Items

- **Frontend lint blocker (out of scope):** `client/src/components/ui/text-input.tsx` contains a pre-existing syntax error at line 43 (`{errors.}`), which causes the full `npm run lint` command to fail.
- **Frontend build blocker (out of scope):** `npm run build` currently fails on the same pre-existing `text-input.tsx` syntax error and a TypeScript parse issue inside `node_modules/react-router/dist/development/index.d.mts` in this environment.
