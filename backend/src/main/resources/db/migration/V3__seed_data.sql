-- Seed Acme Corp organization
INSERT INTO organizations (id, name, slug, org_code, active)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Acme Corp', 'acme-corp', 'ACME', true)
ON CONFLICT DO NOTHING;

-- Seed org settings
INSERT INTO org_settings (id, org_id, invoice_prefix, default_currency, trial_days, payment_terms_days)
VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'ACME', 'USD', 14, 30)
ON CONFLICT DO NOTHING;

-- Seed admin user (password: password123 - bcrypt encoded)
INSERT INTO users (id, org_id, email, password, first_name, last_name, role, active)
VALUES (
    'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    'admin@acme.com',
    '$2a$10$m1yE2SfnzpX8PFCI0fEJ8utg5YvtU3p7A6aigAVZtu6GtgZXw86d6',
    'Admin',
    'User',
    'OWNER',
    true
)
ON CONFLICT DO NOTHING;
