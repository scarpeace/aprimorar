import { describe, it } from "node:test";
import assert from "node:assert/strict";

import { getActiveLinkedStudentsCount } from "./getActiveLinkedStudentsCount";

describe("getActiveLinkedStudentsCount", () => {
  it("returns zero when all linked students are archived", () => {
    const result = getActiveLinkedStudentsCount([
      {
        id: "student-1",
        name: "Aluno Arquivado 1",
        active: false,
      },
      {
        id: "student-2",
        name: "Aluno Arquivado 2",
        active: false,
      },
    ]);

    assert.equal(result, 0);
  });

  it("counts only active linked students when the page mixes active and archived records", () => {
    const result = getActiveLinkedStudentsCount([
      {
        id: "student-1",
        name: "Aluno Ativo 1",
        active: true,
      },
      {
        id: "student-2",
        name: "Aluno Arquivado",
        active: false,
      },
      {
        id: "student-3",
        name: "Aluno Ativo 2",
      },
      {
        id: "student-4",
        name: "Aluno Ativo 3",
        active: undefined,
      },
    ]);

    assert.equal(result, 3);
  });

  it("returns zero safely for missing or empty content", () => {
    assert.equal(getActiveLinkedStudentsCount(undefined), 0);
    assert.equal(getActiveLinkedStudentsCount([]), 0);
  });
});
