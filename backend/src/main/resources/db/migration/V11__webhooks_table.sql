CREATE TABLE webhook_events (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    org_id          UUID        NOT NULL REFERENCES organizations(id),
    event_type      VARCHAR(50) NOT NULL,
    payload         TEXT        NOT NULL,
    status          VARCHAR(20) NOT NULL,
    attempts        INTEGER     NOT NULL DEFAULT 0,
    last_attempt_at TIMESTAMPTZ,
    idempotency_key VARCHAR(255) UNIQUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    version         BIGINT      NOT NULL DEFAULT 0
);

CREATE INDEX idx_webhook_events_org_id ON webhook_events(org_id);
CREATE INDEX idx_webhook_events_status ON webhook_events(status);
