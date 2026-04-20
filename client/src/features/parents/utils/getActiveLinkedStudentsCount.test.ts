import { describe, it } from "node:test";
import assert from "node:assert/strict";

import { getActiveLinkedStudentsCount } from "./getActiveLinkedStudentsCount";

describe("getActiveLinkedStudentsCount", () => {
  it("returns zero when all linked students are archived", () => {
    const result = getActiveLinkedStudentsCount({
      page: 0,
      size: 10,
      totalElements: 2,
      totalPages: 1,
      content: [
        {
          id: "student-1",
          name: "Aluno Arquivado 1",
          archivedAt: "2026-04-20T10:00:00Z",
        },
        {
          id: "student-2",
          name: "Aluno Arquivado 2",
          archivedAt: "2026-04-20T11:00:00Z",
        },
      ],
    });

    assert.equal(result, 0);
  });

  it("counts only active linked students when the page mixes active and archived records", () => {
    const result = getActiveLinkedStudentsCount({
      page: 0,
      size: 10,
      totalElements: 4,
      totalPages: 1,
      content: [
        {
          id: "student-1",
          name: "Aluno Ativo 1",
          archivedAt: null,
        },
        {
          id: "student-2",
          name: "Aluno Arquivado",
          archivedAt: "2026-04-20T11:00:00Z",
        },
        {
          id: "student-3",
          name: "Aluno Ativo 2",
        },
        {
          id: "student-4",
          name: "Aluno Ativo 3",
          archivedAt: undefined,
        },
      ],
    });

    assert.equal(result, 3);
  });

  it("returns zero safely for missing or empty content", () => {
    assert.equal(getActiveLinkedStudentsCount(undefined), 0);
    assert.equal(
      getActiveLinkedStudentsCount({
        page: 0,
        size: 10,
        totalElements: 0,
        totalPages: 0,
        content: [],
      }),
      0,
    );
  });
});
