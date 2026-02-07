# BillPilot API Examples

Base URL: `http://localhost:8080/api/v1`

All authenticated endpoints require the header:
```
Authorization: Bearer <access_token>
```

---

## Auth

### Signup

```bash
curl -X POST http://localhost:8080/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "orgName": "My Company",
    "email": "admin@mycompany.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@acme.com",
    "password": "password123"
  }'
```

### Refresh Token

```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "<refresh_token>"
  }'
```

### Logout

```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Authorization: Bearer <access_token>"
```

---

## Plans

### List Plans

```bash
curl http://localhost:8080/api/v1/plans \
  -H "Authorization: Bearer <access_token>"
```

### Create Plan

```bash
curl -X POST http://localhost:8080/api/v1/plans \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Starter",
    "description": "For small teams",
    "price": 29.00,
    "billingInterval": "MONTHLY",
    "trialDays": 14,
    "features": "5 users, 10GB storage",
    "maxSeats": 5,
    "usageLimit": 1000
  }'
```

### Update Plan

```bash
curl -X PUT http://localhost:8080/api/v1/plans/<plan_id> \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Starter Plus",
    "price": 39.00
  }'
```

### Delete Plan

```bash
curl -X DELETE http://localhost:8080/api/v1/plans/<plan_id> \
  -H "Authorization: Bearer <access_token>"
```

---

## Customers

### List Customers

```bash
curl http://localhost:8080/api/v1/customers \
  -H "Authorization: Bearer <access_token>"
```

### Search Customers

```bash
curl "http://localhost:8080/api/v1/customers?search=Alice&page=0&size=20" \
  -H "Authorization: Bearer <access_token>"
```

### Create Customer

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "company": "Alice Co",
    "phone": "+1-555-0100",
    "address": "123 Main St",
    "city": "Springfield",
    "state": "IL",
    "country": "US",
    "postalCode": "62701"
  }'
```

### Get Customer

```bash
curl http://localhost:8080/api/v1/customers/<customer_id> \
  -H "Authorization: Bearer <access_token>"
```

### Update Customer

```bash
curl -X PUT http://localhost:8080/api/v1/customers/<customer_id> \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson-Smith",
    "phone": "+1-555-0101"
  }'
```

### Delete Customer

```bash
curl -X DELETE http://localhost:8080/api/v1/customers/<customer_id> \
  -H "Authorization: Bearer <access_token>"
```

---

## Subscriptions

### List Subscriptions for Customer

```bash
curl http://localhost:8080/api/v1/subscriptions/customer/<customer_id> \
  -H "Authorization: Bearer <access_token>"
```

### Create Subscription

```bash
curl -X POST http://localhost:8080/api/v1/subscriptions \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "<customer_id>",
    "planId": "<plan_id>",
    "quantity": 1,
    "startTrial": true
  }'
```

### Cancel Subscription

```bash
curl -X POST http://localhost:8080/api/v1/subscriptions/<subscription_id>/cancel \
  -H "Authorization: Bearer <access_token>"
```

### Change Plan

```bash
curl -X POST http://localhost:8080/api/v1/subscriptions/<subscription_id>/change-plan \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "newPlanId": "<new_plan_id>",
    "newQuantity": 2
  }'
```

---

## Invoices

### List Invoices

```bash
curl http://localhost:8080/api/v1/invoices \
  -H "Authorization: Bearer <access_token>"
```

### List Invoices for Customer

```bash
curl http://localhost:8080/api/v1/invoices/customer/<customer_id> \
  -H "Authorization: Bearer <access_token>"
```

### Get Invoice

```bash
curl http://localhost:8080/api/v1/invoices/<invoice_id> \
  -H "Authorization: Bearer <access_token>"
```

### Mark Invoice as Paid

```bash
curl -X POST http://localhost:8080/api/v1/invoices/<invoice_id>/mark-paid \
  -H "Authorization: Bearer <access_token>"
```

### Void Invoice

```bash
curl -X POST http://localhost:8080/api/v1/invoices/<invoice_id>/void \
  -H "Authorization: Bearer <access_token>"
```

---

## Payments

### List Payments

```bash
curl http://localhost:8080/api/v1/payments \
  -H "Authorization: Bearer <access_token>"
```

### List Payments for Customer

```bash
curl http://localhost:8080/api/v1/payments/customer/<customer_id> \
  -H "Authorization: Bearer <access_token>"
```

### Process Payment for Invoice

```bash
curl -X POST http://localhost:8080/api/v1/payments/invoice/<invoice_id> \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "paymentMethod": "credit_card"
  }'
```

---

## Usage

### Record Usage Event

```bash
curl -X POST http://localhost:8080/api/v1/usage \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "subscriptionId": "<subscription_id>",
    "customerId": "<customer_id>",
    "featureKey": "api_calls",
    "quantity": 100
  }'
```

### Get Usage for Subscription

```bash
curl http://localhost:8080/api/v1/usage/subscription/<subscription_id> \
  -H "Authorization: Bearer <access_token>"
```

### Get Usage Summary

```bash
curl "http://localhost:8080/api/v1/usage/subscription/<subscription_id>/summary?featureKey=api_calls" \
  -H "Authorization: Bearer <access_token>"
```

---

## Webhooks

### List Webhook Events

```bash
curl http://localhost:8080/api/v1/webhooks \
  -H "Authorization: Bearer <access_token>"
```

### Get Webhook Event

```bash
curl http://localhost:8080/api/v1/webhooks/<event_id> \
  -H "Authorization: Bearer <access_token>"
```

---

## Users

### List Users

```bash
curl http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer <access_token>"
```

### Invite User

```bash
curl -X POST http://localhost:8080/api/v1/users/invite \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@acme.com",
    "firstName": "New",
    "lastName": "User",
    "role": "BILLING_MGR"
  }'
```

### Update User

```bash
curl -X PUT http://localhost:8080/api/v1/users/<user_id> \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Updated",
    "role": "ADMIN"
  }'
```

### Delete User

```bash
curl -X DELETE http://localhost:8080/api/v1/users/<user_id> \
  -H "Authorization: Bearer <access_token>"
```

---

## Settings

### Get Org Settings

```bash
curl http://localhost:8080/api/v1/settings \
  -H "Authorization: Bearer <access_token>"
```

### Update Org Settings

```bash
curl -X PUT http://localhost:8080/api/v1/settings \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "invoicePrefix": "ACME",
    "defaultCurrency": "USD",
    "trialDays": 14,
    "paymentTermsDays": 30,
    "webhookUrl": "https://example.com/webhooks"
  }'
```

---

## Audit Logs

### List Audit Logs

```bash
curl http://localhost:8080/api/v1/audit-logs \
  -H "Authorization: Bearer <access_token>"
```

### Filter by Entity Type

```bash
curl "http://localhost:8080/api/v1/audit-logs?entityType=PLAN" \
  -H "Authorization: Bearer <access_token>"
```

### Get Logs for Specific Entity

```bash
curl http://localhost:8080/api/v1/audit-logs/entity/<entity_id> \
  -H "Authorization: Bearer <access_token>"
```

---

## Dashboard

### Get Dashboard KPIs

```bash
curl http://localhost:8080/api/v1/dashboard \
  -H "Authorization: Bearer <access_token>"
```

---

## Quick Start

1. Start services: `docker-compose up -d`
2. Run backend: `cd backend && mvn spring-boot:run`
3. Login with seeded admin:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@acme.com","password":"password123"}' | jq -r '.accessToken')

echo $TOKEN
```

4. Use the token for subsequent requests:

```bash
curl http://localhost:8080/api/v1/plans \
  -H "Authorization: Bearer $TOKEN" | jq
```
