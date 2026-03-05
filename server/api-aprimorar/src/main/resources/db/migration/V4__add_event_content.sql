ALTER TABLE tb_events
    ADD COLUMN content VARCHAR(50);

UPDATE tb_events
SET content = CASE
    WHEN LOWER(COALESCE(title, '') || ' ' || COALESCE(description, '')) LIKE '%enem%' THEN 'ENEM'
    WHEN LOWER(COALESCE(title, '') || ' ' || COALESCE(description, '')) LIKE '%fisica%'
      OR LOWER(COALESCE(title, '') || ' ' || COALESCE(description, '')) LIKE '%física%' THEN 'FISICA'
    WHEN LOWER(COALESCE(title, '') || ' ' || COALESCE(description, '')) LIKE '%matematica%'
      OR LOWER(COALESCE(title, '') || ' ' || COALESCE(description, '')) LIKE '%matemática%' THEN 'MATEMATICA'
    WHEN LOWER(COALESCE(title, '') || ' ' || COALESCE(description, '')) LIKE '%mentoria%' THEN 'MENTORIA'
    WHEN LOWER(COALESCE(title, '') || ' ' || COALESCE(description, '')) LIKE '%terapia%' THEN 'TERAPIA'
    ELSE 'OUTRO'
END;

ALTER TABLE tb_events
    ALTER COLUMN content SET NOT NULL;
