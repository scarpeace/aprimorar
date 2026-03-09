ALTER TABLE tb_parent ADD COLUMN IF NOT EXISTS archived_at TIMESTAMPTZ;
ALTER TABLE tb_parent ADD COLUMN IF NOT EXISTS last_reactivated_at TIMESTAMPTZ;

UPDATE tb_parent
SET archived_at = COALESCE(updated_at, created_at, NOW())
WHERE active = FALSE AND archived_at IS NULL;

ALTER TABLE tb_parent DROP COLUMN IF EXISTS active;

CREATE INDEX IF NOT EXISTS idx_tb_parent_archived_at ON tb_parent (archived_at);

ALTER TABLE tb_employee ADD COLUMN IF NOT EXISTS archived_at TIMESTAMPTZ;
ALTER TABLE tb_employee ADD COLUMN IF NOT EXISTS last_reactivated_at TIMESTAMPTZ;

UPDATE tb_employee
SET archived_at = COALESCE(updated_at, created_at, NOW())
WHERE active = FALSE AND archived_at IS NULL;

ALTER TABLE tb_employee DROP COLUMN IF EXISTS active;

CREATE INDEX IF NOT EXISTS idx_tb_employee_archived_at ON tb_employee (archived_at);
