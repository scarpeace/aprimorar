INSERT INTO tb_users (id, username, password, role, active, created_at, updated_at)
SELECT
  '11111111-1111-4111-8111-111111111111',
  'admin@aprimorar.local',
  '$2a$10$4SsnlXg6MUSF5yQ0rnlSJOvT5GfsKMiP1R4or1KeYvhAL1dHfAeQa',
  'ADMIN',
  true,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM tb_users WHERE username = 'admin@aprimorar.local'
);
