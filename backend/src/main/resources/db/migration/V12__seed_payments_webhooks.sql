-- Seed payment for the paid invoice
INSERT INTO payments (id, org_id, invoice_id, customer_id, amount, status, payment_method, transaction_id) VALUES
('c1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 29.00, 'SUCCEEDED', 'card', 'txn_seed_00001')
ON CONFLICT DO NOTHING;

-- Seed webhook events
INSERT INTO webhook_events (id, org_id, event_type, payload, status, attempts, last_attempt_at) VALUES
('d3eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'invoice.paid', '{"invoiceId":"a1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11","amount":29.00}', 'DELIVERED', 1, '2026-01-31T12:00:00Z'),
('d4eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'subscription.created', '{"subscriptionId":"f1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"}', 'DELIVERED', 1, '2026-01-01T00:00:00Z'),
('d5eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'invoice.created', '{"invoiceId":"a2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11","amount":297.00}', 'PENDING', 0, null)
ON CONFLICT DO NOTHING;
