CREATE TABLE tb_transactions
(
    id         UUID           NOT NULL,
    type       VARCHAR(20)    NOT NULL,
    status     VARCHAR(20)    NOT NULL,
    amount     DECIMAL(19, 2) NOT NULL,
    origin     VARCHAR(50)    NOT NULL,
    origin_id  UUID           NOT NULL,
    settled_at TIMESTAMP WITHOUT TIME ZONE,
    category   VARCHAR(50)    NOT NULL,
    CONSTRAINT pk_tb_transactions PRIMARY KEY (id)
);

DROP TABLE tb_general_expenses CASCADE;