## Why

Finance, admin, and authentication screens currently depend on frontend code that no longer matches the backend direction and generated Kubb contracts. Replacing those areas with simple placeholder pages removes broken/stale workflows while keeping navigation targets available for future implementation.

## What Changes

- Remove the existing finance feature implementation and replace it with a single `FinancesPage` placeholder.
- Remove the existing admin user-management implementation and replace it with a single `AdminPage` placeholder.
- Replace the current auth/login implementation with a minimal `LoginPage` placeholder.
- Update app routes so `/finance` and finance subroutes render the placeholder finance page.
- Update app routes so `/admin` and existing admin user route render the placeholder admin page.
- Keep `/login` rendering a placeholder auth page.
- Each placeholder page displays the text `implementando`.

## Capabilities

### New Capabilities
- `placeholder-feature-shells`: Placeholder route shells for finance, admin, and auth areas while those features are rebuilt.

### Modified Capabilities

## Impact

- `client/src/features/finance/**`: remove existing finance feature files except the new placeholder page.
- `client/src/features/admin/**`: remove existing admin feature files except the new placeholder page.
- `client/src/features/auth/**`: remove existing auth feature files except the placeholder login page.
- `client/src/App.tsx`: update lazy imports and route mappings to use `FinancesPage`, `AdminPage`, and `LoginPage`.
- `client/src/components/layout/MainLayout.tsx`: update navigation links to point at the placeholder routes.
- Other components that import finance hooks or admin/auth modules may need stale imports removed or isolated if they block compilation.
