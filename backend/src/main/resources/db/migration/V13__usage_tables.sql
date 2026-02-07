CREATE TABLE usage_events (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    org_id          UUID         NOT NULL REFERENCES organizations(id),
    subscription_id UUID         NOT NULL REFERENCES subscriptions(id),
    customer_id     UUID         NOT NULL REFERENCES customers(id),
    feature_key     VARCHAR(100) NOT NULL,
    quantity        BIGINT       NOT NULL,
    recorded_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    version         BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_usage_events_org_id ON usage_events(org_id);
CREATE INDEX idx_usage_events_subscription_id ON usage_events(subscription_id);
CREATE INDEX idx_usage_events_customer_id ON usage_events(customer_id);
CREATE INDEX idx_usage_events_feature_key ON usage_events(feature_key);
