-- Seed plans for Acme Corp
INSERT INTO plans (id, org_id, name, description, price, billing_interval, trial_days, features, active) VALUES
('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Starter', 'Perfect for small teams getting started', 29.00, 'MONTHLY', 14, 'Up to 5 users, Basic analytics, Email support', true),
('d1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Pro', 'For growing teams that need more power', 99.00, 'MONTHLY', 14, 'Up to 25 users, Advanced analytics, Priority support, API access', true),
('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Enterprise', 'For large organizations with custom needs', 299.00, 'MONTHLY', 30, 'Unlimited users, Custom analytics, Dedicated support, Custom integrations, SLA', true)
ON CONFLICT DO NOTHING;

-- Seed customers for Acme Corp
INSERT INTO customers (id, org_id, name, email, company, phone, country, active) VALUES
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Alice Johnson', 'alice@techstartup.com', 'TechStartup Inc', '+1-555-0101', 'US', true),
('e1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Bob Smith', 'bob@growthco.com', 'GrowthCo', '+1-555-0102', 'US', true),
('e2eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Carol Williams', 'carol@enterprise.com', 'Enterprise Ltd', '+44-20-7946-0958', 'UK', true),
('e3eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'David Brown', 'david@startup.io', 'Startup.io', '+1-555-0104', 'US', true),
('e4eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Eva Martinez', 'eva@globaltech.com', 'GlobalTech', '+49-30-123456', 'DE', true)
ON CONFLICT DO NOTHING;
