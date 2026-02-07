CREATE TABLE plans (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    org_id           UUID           NOT NULL REFERENCES organizations(id),
    name             VARCHAR(255)   NOT NULL,
    description      TEXT,
    price            NUMERIC(10,2)  NOT NULL,
    billing_interval VARCHAR(20)    NOT NULL,
    trial_days       INTEGER        NOT NULL DEFAULT 0,
    features         TEXT,
    active           BOOLEAN        NOT NULL DEFAULT true,
    max_seats        INTEGER,
    usage_limit      BIGINT,
    created_at       TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ    NOT NULL DEFAULT now(),
    version          BIGINT         NOT NULL DEFAULT 0
);

CREATE INDEX idx_plans_org_id ON plans(org_id);
