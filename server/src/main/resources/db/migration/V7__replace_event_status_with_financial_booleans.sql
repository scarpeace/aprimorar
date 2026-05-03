ALTER TABLE tb_events ADD COLUMN student_charged BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE tb_events ADD COLUMN employee_paid BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE tb_events
SET student_charged = CASE WHEN income_status = 'PAID' THEN TRUE ELSE FALSE END,
    employee_paid = CASE WHEN expense_status = 'PAID' THEN TRUE ELSE FALSE END;

ALTER TABLE tb_events DROP COLUMN status;
ALTER TABLE tb_events DROP COLUMN income_status;
ALTER TABLE tb_events DROP COLUMN expense_status;
