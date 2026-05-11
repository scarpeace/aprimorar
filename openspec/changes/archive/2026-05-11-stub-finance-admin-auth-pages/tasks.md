## 1. Placeholder Pages

- [x] 1.1 Replace `client/src/features/finance` with a single `pages/FinancesPage.tsx` component that renders `implementando`.
- [x] 1.2 Replace `client/src/features/admin` with a single `pages/AdminPage.tsx` component that renders `implementando`.
- [x] 1.3 Replace `client/src/features/auth/pages/LoginPage.tsx` with a minimal placeholder that renders `implementando`.

## 2. Routes and Navigation

- [x] 2.1 Update `client/src/App.tsx` to import `FinancesPage`, `AdminPage`, and `LoginPage`.
- [x] 2.2 Route `/finance`, `/finance/expenses`, and `/finance/settlement` to `FinancesPage`.
- [x] 2.3 Route `/admin` and `/admin/users` to `AdminPage`.
- [x] 2.4 Keep `/login` routed to `LoginPage`.
- [x] 2.5 Update sidebar navigation to point Finance to `/finance` and admin/user access to `/admin`.

## 3. Stale Import Cleanup

- [x] 3.1 Remove or rewrite source imports that reference deleted finance modules outside `client/src/features/finance`.
- [x] 3.2 Remove source imports that reference deleted admin/auth implementation modules outside their placeholder pages.
- [x] 3.3 Confirm no remaining non-generated source file imports deleted finance/admin/auth files.

## 4. Verification

- [x] 4.1 Run `npm run lint` in `client/`.
- [x] 4.2 Run `npm run build` in `client/` or document any remaining unrelated backend/Kubb contract errors.
- [x] 4.3 Verify `openspec validate stub-finance-admin-auth-pages` succeeds.
