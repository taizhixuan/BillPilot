export interface PagedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface User {
  id: string;
  orgId: string;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  active: boolean;
  createdAt: string;
}

export type Role = 'OWNER' | 'ADMIN' | 'BILLING_MGR' | 'SUPPORT' | 'READ_ONLY';

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}

export interface Plan {
  id: string;
  orgId: string;
  name: string;
  description: string;
  price: number;
  billingInterval: BillingInterval;
  trialDays: number;
  features: string;
  active: boolean;
  maxSeats: number | null;
  usageLimit: number | null;
  createdAt: string;
}

export type BillingInterval = 'MONTHLY' | 'YEARLY' | 'ONE_TIME';

export interface Customer {
  id: string;
  orgId: string;
  name: string;
  email: string;
  company: string | null;
  phone: string | null;
  address: string | null;
  city: string | null;
  state: string | null;
  country: string | null;
  postalCode: string | null;
  notes: string | null;
  active: boolean;
  createdAt: string;
}

export type SubscriptionStatus = 'ACTIVE' | 'TRIALING' | 'PAST_DUE' | 'CANCELED' | 'EXPIRED';

export interface Subscription {
  id: string;
  orgId: string;
  customerId: string;
  customerName: string;
  planId: string;
  planName: string;
  status: SubscriptionStatus;
  quantity: number;
  currentPeriodStart: string;
  currentPeriodEnd: string;
  trialEnd: string | null;
  canceledAt: string | null;
  createdAt: string;
}

export type InvoiceStatus = 'DRAFT' | 'OPEN' | 'PAID' | 'VOID' | 'PAST_DUE';

export interface Invoice {
  id: string;
  orgId: string;
  customerId: string;
  customerName: string;
  subscriptionId: string | null;
  invoiceNumber: string;
  status: InvoiceStatus;
  subtotal: number;
  tax: number;
  total: number;
  dueDate: string;
  paidAt: string | null;
  lineItems: InvoiceLineItem[];
  createdAt: string;
}

export interface InvoiceLineItem {
  id: string;
  description: string;
  quantity: number;
  unitPrice: number;
  amount: number;
}

export type PaymentStatus = 'SUCCEEDED' | 'FAILED' | 'PENDING' | 'REFUNDED';

export interface Payment {
  id: string;
  orgId: string;
  invoiceId: string;
  invoiceNumber: string;
  customerId: string;
  customerName: string;
  amount: number;
  status: PaymentStatus;
  paymentMethod: string;
  transactionId: string;
  failureReason: string | null;
  createdAt: string;
}

export type WebhookEventStatus = 'PENDING' | 'DELIVERED' | 'FAILED';

export interface WebhookEvent {
  id: string;
  orgId: string;
  eventType: string;
  payload: string;
  status: WebhookEventStatus;
  attempts: number;
  lastAttemptAt: string | null;
  createdAt: string;
}

export interface UsageEvent {
  id: string;
  orgId: string;
  subscriptionId: string;
  customerId: string;
  featureKey: string;
  quantity: number;
  recordedAt: string;
}

export interface AuditLog {
  id: string;
  orgId: string;
  userId: string;
  userEmail: string;
  action: string;
  entityType: string;
  entityId: string;
  details: string;
  createdAt: string;
}

export interface DashboardStats {
  mrr: number;
  activeSubscriptions: number;
  churnRate: number;
  overdueInvoices: number;
  revenueTimeline: { date: string; revenue: number }[];
}

export interface OrgSettings {
  id: string;
  orgId: string;
  invoicePrefix: string;
  defaultCurrency: string;
  trialDays: number;
  paymentTermsDays: number;
  webhookUrl: string | null;
}
