ALTER TABLE tb_student
    ADD COLUMN archived_at TIMESTAMPTZ,
    ADD COLUMN last_reactivated_at TIMESTAMPTZ;

UPDATE tb_student
SET archived_at = CASE
    WHEN active IS FALSE THEN COALESCE(updated_at, created_at)
    ELSE NULL
END;
