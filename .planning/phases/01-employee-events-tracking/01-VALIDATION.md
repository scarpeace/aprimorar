---
phase: 01
slug: employee-events-tracking
status: draft
nyquist_compliant: false
wave_0_complete: false
created: 2026-04-28
---

# Phase 01 — Validation Strategy

> Per-phase validation contract for feedback sampling during execution.

---

## Test Infrastructure

| Property | Value |
|----------|-------|
| **Framework** | Spring Boot Starter Test (Backend) / Manual UI tests |
| **Config file** | `pom.xml` |
| **Quick run command** | `cd server/api-aprimorar && ./mvnw test -Dtest=EventControllerTest` |
| **Full suite command** | `cd server/api-aprimorar && ./mvnw test` |
| **Estimated runtime** | ~10 seconds |

---

## Sampling Rate

- **After every task commit:** Run `cd server/api-aprimorar && ./mvnw test -Dtest=EventControllerTest`
- **After every plan wave:** Run `cd server/api-aprimorar && ./mvnw test`
- **Before `/gsd-verify-work`:** Full suite must be green
- **Max feedback latency:** 10 seconds

---

## Per-Task Verification Map

| Task ID | Plan | Wave | Requirement | Threat Ref | Secure Behavior | Test Type | Automated Command | File Exists | Status |
|---------|------|------|-------------|------------|-----------------|-----------|-------------------|-------------|--------|
| 01-01-01 | 01 | 1 | TAB-01 | — | N/A | integration | `mvn test -Dtest=EventControllerTest#testSorting` | ❌ W0 | ⬜ pending |
| 01-01-02 | 01 | 1 | TAB-02 | — | N/A | integration | `mvn test -Dtest=EventControllerTest#testStudentSearch` | ❌ W0 | ⬜ pending |

*Status: ⬜ pending · ✅ green · ❌ red · ⚠️ flaky*

---

## Wave 0 Requirements

- [ ] `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventControllerTest.java` — Need to verify new `studentName` parameter filters correctly.

---

## Manual-Only Verifications

| Behavior | Requirement | Why Manual | Test Instructions |
|----------|-------------|------------|-------------------|
| Search Debouncing | TAB-02 | UI functionality relies on manual verification | Type in search bar, ensure network request triggers after delay. |
| Empty State | TAB-01 | Visual requirement | Search for non-existent student, verify "Nenhum atendimento encontrado" appears within table. |

---

## Validation Sign-Off

- [ ] All tasks have `<automated>` verify or Wave 0 dependencies
- [ ] Sampling continuity: no 3 consecutive tasks without automated verify
- [ ] Wave 0 covers all MISSING references
- [ ] No watch-mode flags
- [ ] Feedback latency < 10s
- [ ] `nyquist_compliant: true` set in frontmatter

**Approval:** pending