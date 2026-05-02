-- V3.1__add_event_status.sql
ALTER TABLE tb_events ADD COLUMN employee_charged BOOLEAN DEFAULT NULL;
ALTER TABLE tb_events ADD COLUMN employee_payment_date TIMESTAMP DEFAULT NULL;
ALTER TABLE tb_events ADD COLUMN student_charge_date TIMESTAMP DEFAULT NULL;
