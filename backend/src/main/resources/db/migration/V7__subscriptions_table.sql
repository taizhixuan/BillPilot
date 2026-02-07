CREATE TABLE subscriptions (
    id                    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    org_id                UUID         NOT NULL REFERENCES organizations(id),
    customer_id           UUID         NOT NULL REFERENCES customers(id),
    plan_id               UUID         NOT NULL REFERENCES plans(id),
    status                VARCHAR(20)  NOT NULL,
    quantity              INTEGER      NOT NULL DEFAULT 1,
    current_period_start  TIMESTAMPTZ  NOT NULL,
    current_period_end    TIMESTAMPTZ  NOT NULL,
    trial_end             TIMESTAMPTZ,
    canceled_at           TIMESTAMPTZ,
    created_at            TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at            TIMESTAMPTZ  NOT NULL DEFAULT now(),
    version               BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_subscriptions_org_id ON subscriptions(org_id);
CREATE INDEX idx_subscriptions_customer_id ON subscriptions(customer_id);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);
