CREATE TABLE IF NOT EXISTS tb_users (
  id uuid NOT NULL,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  role varchar(32) NOT NULL CHECK (role in ('STUDENT', 'EMPLOYEE', 'PARENT', 'ADMIN')),
  active boolean NOT NULL DEFAULT true,
  created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT uk_tb_users_username UNIQUE (username)
);
