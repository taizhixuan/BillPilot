CREATE TABLE payments (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    org_id          UUID           NOT NULL REFERENCES organizations(id),
    invoice_id      UUID           NOT NULL REFERENCES invoices(id),
    customer_id     UUID           NOT NULL REFERENCES customers(id),
    amount          NUMERIC(10,2)  NOT NULL,
    status          VARCHAR(20)    NOT NULL,
    payment_method  VARCHAR(50)    NOT NULL DEFAULT 'card',
    transaction_id  VARCHAR(100)   UNIQUE,
    failure_reason  VARCHAR(500),
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT now(),
    version         BIGINT         NOT NULL DEFAULT 0
);

CREATE INDEX idx_payments_org_id ON payments(org_id);
CREATE INDEX idx_payments_invoice_id ON payments(invoice_id);
CREATE INDEX idx_payments_customer_id ON payments(customer_id);
