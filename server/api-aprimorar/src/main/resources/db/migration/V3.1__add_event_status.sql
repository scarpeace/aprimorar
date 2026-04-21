-- V3.1__add_event_status.sql
ALTER TABLE tb_events ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED';
