# Phase 4: Núcleo de Acompanhamento Financeiro - Research

**Researched:** 2026-04-21
**Domain:** Financial Tracking / Billing / Payments
**Confidence:** HIGH

## Summary

This phase implements a dedicated financial tracking module to replace informal spreadsheets. The core architecture centers on a **Billing (Cobrança)** and **Payment (Pagamento)** ledger system. Billing records represent the obligation (e.g., monthly fees, materials, event fees), while Payment records track the actual cash flow.

The implementation will focus on precision (using `BigDecimal` throughout), clear status transitions (PENDING -> PARTIAL -> PAID), and centralized aggregation for financial reporting.

**Primary recommendation:** Use a dedicated `finance` domain package in the backend and a corresponding `finance` feature folder in the frontend to keep these critical business rules isolated from operational concerns like student registration or event scheduling.

<phase_requirements>
## Phase Requirements

| ID | Description | Research Support |
|----|-------------|------------------|
| FIN-01 | A secretária pode criar um registro de cobrança vinculado ao aluno ou parte responsável correta | Domain model for `Billing` entity and link to `Parent`/`Student`. |
| FIN-02 | A secretária pode registrar um pagamento com valor e data de pagamento contra uma cobrança existente | Domain model for `Payment` entity and status update logic. |
| FIN-03 | A secretária pode visualizar resumos financeiros básicos para o saldo total em aberto, valores recebidos e totais de cobranças | SQL aggregation patterns and `FinancialSummaryDTO` design. |
</phase_requirements>

## Architectural Responsibility Map

| Capability | Primary Tier | Secondary Tier | Rationale |
|------------|-------------|----------------|-----------|
| Financial Ledger | Database | — | The database is the source of truth for all transactions and historical balances. |
| Transactional Integrity | API / Backend | — | Ensuring payments are correctly applied to billings and status is updated atomically. |
| Reporting / Aggregations | API / Backend | Browser / Client | Backend performs high-performance SUM/COUNT; Frontend handles chart rendering. |
| Financial Data Entry | Browser / Client | — | Capturing user input for new billings and payments with immediate UI validation. |

## Standard Stack

### Core
| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| java.math.BigDecimal | Standard | Monetary arithmetic | Prevents floating-point errors (precision 19, scale 2). |
| react-hook-form | 7.71.2 | Form handling | Standard in project for robust data entry. |
| zod | 4.3.6 | Validation | Schema-first validation for financial rules (e.g., positive amounts). |
| use-mask-input | 3.7.4 | Input masking | Critical for consistent currency input (BRL). |

### Supporting
| Library | Version | Purpose | When to Use |
|---------|---------|---------|--------------|
| recharts | 3.8.0 | Visualization | Financial summaries and evolution charts. |
| Intl.NumberFormat | Standard | Formatting | Displaying values as `R$ 1.234,56` consistently. |

**Installation:**
The required libraries are already installed in the project.

## Architecture Patterns

### Recommended Project Structure
```
server/src/main/java/com/aprimorar/api/domain/finance/
├── Billing.java           # Entity for obligations
├── Payment.java           # Entity for cash flow
├── BillingStatus.java     # Enum (PENDING, PARTIAL, PAID, CANCELLED, OVERDUE)
├── BillingService.java    # Status transition logic
├── PaymentService.java    # Logic for applying payments
└── dto/                   # Request/Response/Summary objects

client/src/features/finance/
├── components/            # SummaryCards, BillingTable, PaymentModal
├── forms/                 # BillingForm, PaymentForm
├── hooks/                 # useFinanceSummary, useBillings
└── pages/                 # FinanceDashboardPage, BillingDetailsPage
```

### Pattern 1: Automatic Status Calculation
Instead of letting the user set the status manually (which leads to errors), the status of a `Billing` should be derived from the sum of its `Payments`.
- `PAID`: `sum(payments.amount) >= billing.totalAmount`
- `PARTIAL`: `0 < sum(payments.amount) < billing.totalAmount`
- `PENDING`: `sum(payments.amount) == 0`
- `OVERDUE`: `status != PAID` and `dueDate < today`

### Anti-Patterns to Avoid
- **Floating Point for Money:** Never use `double` or `float` for `amount` fields. [VERIFIED: industry standard]
- **Deleting Financial Records:** Don't allow physical deletion of Billing/Payment records once they have linked transactions. Use a `CANCELLED` status or soft-delete.
- **Client-Side Calculations for Totals:** Never rely on the frontend to calculate the total balance. The API must provide the aggregate value to ensure consistency across different views.

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Currency Masking | Manual regex | `use-mask-input` | Handles backspacing, decimal points, and prefixing correctly. |
| Currency Formatting | Manual string concat | `Intl.NumberFormat` | Standardized locale support (pt-BR). |
| Financial Graphs | Manual SVG/Canvas | `recharts` | Already integrated and accessible. |

## Common Pitfalls

### Pitfall 1: Floating Point Errors
**What goes wrong:** `0.1 + 0.2` becomes `0.30000000000000004` in the database or UI.
**Why it happens:** Binary representation of decimals.
**How to avoid:** Use `BigDecimal` in Java with String constructor and `numeric(19,2)` in PostgreSQL.

### Pitfall 2: Timezone Shift on Due Dates
**What goes wrong:** A bill due on "2026-05-10" shows as "2026-05-09" for some users.
**Why it happens:** Using `Instant` or `UTC` timestamps for a concept that is purely a calendar date.
**How to avoid:** Use `LocalDate` for `dueDate` and `paymentDate`.

### Pitfall 3: Inconsistent Status updates
**What goes wrong:** A payment is added but the billing stays "PENDING".
**Why it happens:** Logic updated the Payment table but forgot the Billing table.
**How to avoid:** Encapsulate payment registration in a `@Transactional` service method that updates both.

## Code Examples

### Java: Correct BigDecimal Initialization
```java
// Source: Official Java Docs / Industry Standard
BigDecimal amount = new BigDecimal("150.00"); // Use String constructor
BigDecimal total = BigDecimal.ZERO;
total = total.add(amount); // BigDecimal is immutable
if (total.compareTo(BigDecimal.valueOf(100)) > 0) { ... } // Use compareTo
```

### TypeScript: Currency Formatting
```typescript
// Source: Project Utils (formatter.ts)
export const brl = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

console.log(brl.format(1234.56)); // "R$ 1.234,56"
```

## Assumptions Log

| # | Claim | Section | Risk if Wrong |
|---|-------|---------|---------------|
| A1 | Only BRL currency is needed | Summary | Low - System can be refactored later if multi-currency is required. |
| A2 | Authenticated users can access all finance data | Security | Medium - Might need specific "ADMIN" role later. |

## Environment Availability

| Dependency | Required By | Available | Version | Fallback |
|------------|------------|-----------|---------|----------|
| PostgreSQL | Persistence | ✓ | 15+ | — |
| Java | Backend | ✓ | 21 | — |
| Node/npm | Frontend | ✓ | 20+ | — |

## Validation Architecture

### Test Framework
| Property | Value |
|----------|-------|
| Framework | JUnit 5 / Spring Boot Test |
| Config file | `pom.xml` |
| Quick run command | `./mvnw test -Dtest=com.aprimorar.api.domain.finance.*` |
| Full suite command | `./mvnw test` |

### Phase Requirements → Test Map
| Req ID | Behavior | Test Type | Automated Command | File Exists? |
|--------|----------|-----------|-------------------|-------------|
| FIN-01 | Create Billing with valid data | Unit | `./mvnw test -Dtest=BillingTest` | ❌ Wave 0 |
| FIN-02 | Payment updates Billing status | Unit | `./mvnw test -Dtest=PaymentServiceTest` | ❌ Wave 0 |
| FIN-03 | Summary calculates correct totals | Integration | `./mvnw test -Dtest=FinanceSummaryTest` | ❌ Wave 0 |

## Security Domain

### Applicable ASVS Categories

| ASVS Category | Applies | Standard Control |
|---------------|---------|-----------------|
| V4 Access Control | yes | Verify user session and internal staff access to `/v1/finance/**`. |
| V5 Input Validation | yes | Strict Zod schemas for amounts (positive) and dates (not too far in future). |

### Known Threat Patterns for Finance

| Pattern | STRIDE | Standard Mitigation |
|---------|--------|---------------------|
| Parameter Tampering | Tampering | Validating that the Billing exists and belongs to the specified Parent/Student. |
| Negative Amount Attack | Tampering | Using `@Positive` validation on all amount fields. |

## Sources

### Primary (HIGH confidence)
- Official Java `BigDecimal` Docs - Precision and arithmetic rules.
- Project `STACK.md` - Framework versions and existing libraries.
- `V1__init.sql` - Database patterns for existing entities.

### Secondary (MEDIUM confidence)
- Web search for "Spring Boot financial tracking data model" - Industry standard patterns for ledger systems.

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH - Libraries already in use and verified.
- Architecture: HIGH - Standard ledger pattern.
- Pitfalls: HIGH - Common financial software issues documented.

**Research date:** 2026-04-21
**Valid until:** 2026-05-21
