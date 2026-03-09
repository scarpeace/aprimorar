ALTER TABLE tb_parent DROP CONSTRAINT IF EXISTS uk_parent_name;
ALTER TABLE tb_parent DROP CONSTRAINT IF EXISTS uk_parent_contact;

ALTER TABLE tb_student DROP CONSTRAINT IF EXISTS uk_student_name;
ALTER TABLE tb_student DROP CONSTRAINT IF EXISTS uk_student_contact;

ALTER TABLE tb_employee DROP CONSTRAINT IF EXISTS uk_employee_name;
ALTER TABLE tb_employee DROP CONSTRAINT IF EXISTS uk_employee_contact;
