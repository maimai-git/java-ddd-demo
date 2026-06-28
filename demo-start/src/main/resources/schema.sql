CREATE TABLE IF NOT EXISTS t_skill (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    category    VARCHAR(50)   NOT NULL,
    level       VARCHAR(20)   NOT NULL DEFAULT 'BEGINNER',
    description TEXT,
    status      VARCHAR(20)   NOT NULL DEFAULT 'DRAFT',
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ   NOT NULL DEFAULT now()
);

-- 兼容已有库：添加 status 列
ALTER TABLE t_skill ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'DRAFT';

CREATE TABLE IF NOT EXISTS t_order (
    id           BIGSERIAL PRIMARY KEY,
    order_no     VARCHAR(64)   NOT NULL,
    province     VARCHAR(50),
    city         VARCHAR(50),
    district     VARCHAR(50),
    detail       VARCHAR(200),
    total_amount NUMERIC(10, 2),
    currency     VARCHAR(5),
    status       VARCHAR(20)   NOT NULL DEFAULT 'NEW',
    created_at   TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT uk_order_no UNIQUE (order_no)
);

CREATE TABLE IF NOT EXISTS t_project (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    description TEXT,
    status      VARCHAR(20)    NOT NULL DEFAULT 'DRAFT',
    visibility  VARCHAR(20),
    priority    INT            DEFAULT 0,
    deadline    DATE,
    tags_json   TEXT           DEFAULT '[]',
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS t_project_stage (
    id            BIGSERIAL PRIMARY KEY,
    project_id    BIGINT        NOT NULL,
    name          VARCHAR(100)  NOT NULL,
    display_order INT           DEFAULT 0,
    status        VARCHAR(20)   NOT NULL DEFAULT 'TODO',
    CONSTRAINT fk_stage_project FOREIGN KEY (project_id) REFERENCES t_project(id)
);

CREATE TABLE IF NOT EXISTS t_order_item (
    id           BIGSERIAL PRIMARY KEY,
    order_id     BIGINT        NOT NULL,
    product_id   BIGINT        NOT NULL,
    product_name VARCHAR(100)  NOT NULL,
    price        NUMERIC(10, 2) NOT NULL,
    currency     VARCHAR(5)    NOT NULL DEFAULT 'CNY',
    quantity     INT           NOT NULL DEFAULT 1,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES t_order(id)
);
