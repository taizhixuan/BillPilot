CREATE TABLE customers (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    org_id      UUID         NOT NULL REFERENCES organizations(id),
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    company     VARCHAR(255),
    phone       VARCHAR(50),
    address     VARCHAR(500),
    city        VARCHAR(100),
    state       VARCHAR(100),
    country     VARCHAR(100),
    postal_code VARCHAR(20),
    notes       TEXT,
    active      BOOLEAN      NOT NULL DEFAULT true,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    version     BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_customers_org_id ON customers(org_id);
CREATE INDEX idx_customers_email ON customers(email);
