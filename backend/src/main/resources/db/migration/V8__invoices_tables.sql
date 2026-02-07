CREATE TABLE invoice_number_sequences (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    org_id      UUID    NOT NULL REFERENCES organizations(id),
    year        INTEGER NOT NULL,
    last_number INTEGER NOT NULL DEFAULT 0,
    UNIQUE(org_id, year)
);

CREATE TABLE invoices (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    org_id          UUID           NOT NULL REFERENCES organizations(id),
    customer_id     UUID           NOT NULL REFERENCES customers(id),
    subscription_id UUID           REFERENCES subscriptions(id),
    invoice_number  VARCHAR(30)    NOT NULL UNIQUE,
    status          VARCHAR(20)    NOT NULL,
    subtotal        NUMERIC(10,2)  NOT NULL,
    tax             NUMERIC(10,2)  NOT NULL DEFAULT 0,
    total           NUMERIC(10,2)  NOT NULL,
    due_date        DATE           NOT NULL,
    paid_at         TIMESTAMPTZ,
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT now(),
    version         BIGINT         NOT NULL DEFAULT 0
);

CREATE INDEX idx_invoices_org_id ON invoices(org_id);
CREATE INDEX idx_invoices_customer_id ON invoices(customer_id);
CREATE INDEX idx_invoices_status ON invoices(status);
CREATE INDEX idx_invoices_invoice_number ON invoices(invoice_number);

CREATE TABLE invoice_line_items (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    invoice_id  UUID          NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    description VARCHAR(500)  NOT NULL,
    quantity    INTEGER       NOT NULL,
    unit_price  NUMERIC(10,2) NOT NULL,
    amount      NUMERIC(10,2) NOT NULL
);

CREATE INDEX idx_invoice_line_items_invoice_id ON invoice_line_items(invoice_id);
