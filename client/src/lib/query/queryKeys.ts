export const queryKeys = {
  students: ["students"],
  studentDetail: (id: string) => ["students", id],
  studentsByParent: (id: string) => ["students", "parent", id],
  employees: ["employees"],
  parents: ["parents"],
  parentDetail: (id: string) => ["parents", id],
  events: ["events"],
  event: (id: string) => ["events", id],
  dashboard: ["dashboard"],
} as const
