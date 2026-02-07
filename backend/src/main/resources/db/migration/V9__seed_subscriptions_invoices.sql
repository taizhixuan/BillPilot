-- Seed invoice number sequence
INSERT INTO invoice_number_sequences (id, org_id, year, last_number)
VALUES ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 2026, 3)
ON CONFLICT DO NOTHING;

-- Seed subscriptions
INSERT INTO subscriptions (id, org_id, customer_id, plan_id, status, quantity, current_period_start, current_period_end) VALUES
('f1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'ACTIVE', 1, '2026-01-01T00:00:00Z', '2026-02-01T00:00:00Z'),
('f2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'd1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'ACTIVE', 3, '2026-01-15T00:00:00Z', '2026-02-15T00:00:00Z'),
('f3eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'ACTIVE', 10, '2026-01-10T00:00:00Z', '2026-02-10T00:00:00Z')
ON CONFLICT DO NOTHING;

-- Seed invoices
INSERT INTO invoices (id, org_id, customer_id, subscription_id, invoice_number, status, subtotal, tax, total, due_date) VALUES
('a1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'f1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'ACME-2026-00001', 'PAID', 29.00, 0, 29.00, '2026-01-31'),
('a2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'f2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'ACME-2026-00002', 'OPEN', 297.00, 0, 297.00, '2026-02-15'),
('a3eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'f3eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'ACME-2026-00003', 'OPEN', 2990.00, 0, 2990.00, '2026-02-10')
ON CONFLICT DO NOTHING;

-- Seed invoice line items
INSERT INTO invoice_line_items (id, invoice_id, description, quantity, unit_price, amount) VALUES
('b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Starter - MONTHLY (x1)', 1, 29.00, 29.00),
('b2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Pro - MONTHLY (x3)', 3, 99.00, 297.00),
('b3eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a3eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Enterprise - MONTHLY (x10)', 10, 299.00, 2990.00)
ON CONFLICT DO NOTHING;
