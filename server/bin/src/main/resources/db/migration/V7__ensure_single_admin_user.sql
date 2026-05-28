CREATE UNIQUE INDEX IF NOT EXISTS uk_tb_users_single_admin
ON tb_users (role)
WHERE role = 'ADMIN';
