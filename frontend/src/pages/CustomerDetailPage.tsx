import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useCustomer } from '../hooks/useCustomers';
import { useCustomerSubscriptions, useCreateSubscription, useCancelSubscription } from '../hooks/useSubscriptions';
import { useCustomerInvoices } from '../hooks/useInvoices';
import { useCustomerPayments } from '../hooks/usePayments';
import { usePlans } from '../hooks/usePlans';
import { Badge } from '../components/Badge';
import { DataTable, type Column } from '../components/DataTable';
import { Modal } from '../components/Modal';
import { LoadingSkeleton } from '../components/LoadingSkeleton';
import { ArrowLeftIcon, PlusIcon } from '@heroicons/react/24/outline';
import { cn } from '../utils/cn';
import type { Subscription, Invoice, Payment } from '../api/types';

type Tab = 'subscriptions' | 'invoices' | 'payments';

export function CustomerDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [tab, setTab] = useState<Tab>('subscriptions');
  const [subModalOpen, setSubModalOpen] = useState(false);
  const { data: customer, isLoading } = useCustomer(id!);

  if (isLoading) return <LoadingSkeleton rows={8} />;
  if (!customer) return <p className="text-slate-500">Customer not found</p>;

  const tabs: { key: Tab; label: string }[] = [
    { key: 'subscriptions', label: 'Subscriptions' },
    { key: 'invoices', label: 'Invoices' },
    { key: 'payments', label: 'Payments' },
  ];

  return (
    <div>
      <button onClick={() => navigate('/customers')} className="inline-flex items-center gap-1 text-sm text-slate-500 hover:text-slate-700 mb-4">
        <ArrowLeftIcon className="h-4 w-4" /> Back to Customers
      </button>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-semibold text-slate-900">{customer.name}</h1>
          <p className="text-sm text-slate-500">{customer.email} {customer.company && `- ${customer.company}`}</p>
        </div>
        <Badge status={customer.active ? 'ACTIVE' : 'CANCELED'}>{customer.active ? 'Active' : 'Inactive'}</Badge>
      </div>

      <div className="flex gap-1 mb-6 border-b border-slate-200">
        {tabs.map(t => (
          <button key={t.key} onClick={() => setTab(t.key)} className={cn('px-4 py-2 text-sm font-medium border-b-2 -mb-px transition-colors', tab === t.key ? 'border-primary-500 text-primary-600' : 'border-transparent text-slate-500 hover:text-slate-700')}>
            {t.label}
          </button>
        ))}
      </div>

      {tab === 'subscriptions' && <SubscriptionsTab customerId={id!} onNew={() => setSubModalOpen(true)} />}
      {tab === 'invoices' && <InvoicesTab customerId={id!} />}
      {tab === 'payments' && <PaymentsTab customerId={id!} />}

      <NewSubscriptionModal open={subModalOpen} customerId={id!} onClose={() => setSubModalOpen(false)} />
    </div>
  );
}

function SubscriptionsTab({ customerId, onNew }: { customerId: string; onNew: () => void }) {
  const { data } = useCustomerSubscriptions(customerId);
  const cancel = useCancelSubscription();
  const cols: Column<Subscription>[] = [
    { key: 'plan', header: 'Plan', render: (s) => <span className="font-medium">{s.planName}</span> },
    { key: 'qty', header: 'Qty', render: (s) => s.quantity },
    { key: 'status', header: 'Status', render: (s) => <Badge status={s.status}>{s.status}</Badge> },
    { key: 'period', header: 'Period End', render: (s) => new Date(s.currentPeriodEnd).toLocaleDateString() },
    { key: 'actions', header: '', render: (s) => s.status === 'ACTIVE' ? <button onClick={(e) => { e.stopPropagation(); cancel.mutate(s.id); }} className="text-xs text-red-500 hover:text-red-700">Cancel</button> : null },
  ];
  return (
    <div>
      <div className="flex justify-end mb-3">
        <button onClick={onNew} className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-primary-500 text-white rounded-lg text-xs font-medium hover:bg-primary-600"><PlusIcon className="h-3.5 w-3.5" /> New Subscription</button>
      </div>
      <DataTable columns={cols} data={data?.content ?? []} keyExtractor={s => s.id} />
    </div>
  );
}

function InvoicesTab({ customerId }: { customerId: string }) {
  const navigate = useNavigate();
  const { data } = useCustomerInvoices(customerId);
  const cols: Column<Invoice>[] = [
    { key: 'num', header: 'Invoice #', render: (i) => <span className="font-mono text-xs">{i.invoiceNumber}</span> },
    { key: 'total', header: 'Total', render: (i) => `$${Number(i.total).toLocaleString()}` },
    { key: 'due', header: 'Due', render: (i) => i.dueDate },
    { key: 'status', header: 'Status', render: (i) => <Badge status={i.status}>{i.status}</Badge> },
  ];
  return <DataTable columns={cols} data={data?.content ?? []} keyExtractor={i => i.id} onRowClick={i => navigate(`/invoices/${i.id}`)} />;
}

function PaymentsTab({ customerId }: { customerId: string }) {
  const { data } = useCustomerPayments(customerId);
  const cols: Column<Payment>[] = [
    { key: 'txn', header: 'Transaction', render: (p) => <span className="font-mono text-xs">{p.transactionId}</span> },
    { key: 'amount', header: 'Amount', render: (p) => `$${Number(p.amount).toLocaleString()}` },
    { key: 'method', header: 'Method', render: (p) => p.paymentMethod },
    { key: 'status', header: 'Status', render: (p) => <Badge status={p.status}>{p.status}</Badge> },
    { key: 'date', header: 'Date', render: (p) => new Date(p.createdAt).toLocaleDateString() },
  ];
  return <DataTable columns={cols} data={data?.content ?? []} keyExtractor={p => p.id} />;
}

function NewSubscriptionModal({ open, customerId, onClose }: { open: boolean; customerId: string; onClose: () => void }) {
  const { data: plansData } = usePlans(0, 100);
  const create = useCreateSubscription();
  const [form, setForm] = useState({ planId: '', quantity: '1', startTrial: false });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await create.mutateAsync({ customerId, planId: form.planId, quantity: parseInt(form.quantity), startTrial: form.startTrial });
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose} title="New Subscription">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Plan</label>
          <select value={form.planId} onChange={e => setForm(f => ({ ...f, planId: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" required>
            <option value="">Select a plan...</option>
            {plansData?.content.filter(p => p.active).map(p => (<option key={p.id} value={p.id}>{p.name} - ${p.price}/mo</option>))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Quantity / Seats</label>
          <input type="number" min="1" value={form.quantity} onChange={e => setForm(f => ({ ...f, quantity: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" />
        </div>
        <label className="flex items-center gap-2">
          <input type="checkbox" checked={form.startTrial} onChange={e => setForm(f => ({ ...f, startTrial: e.target.checked }))} className="rounded border-slate-300" />
          <span className="text-sm text-slate-700">Start with trial period</span>
        </label>
        <div className="flex justify-end gap-2 pt-2">
          <button type="button" onClick={onClose} className="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg">Cancel</button>
          <button type="submit" className="px-4 py-2 bg-primary-500 text-white rounded-lg text-sm font-medium hover:bg-primary-600">Create Subscription</button>
        </div>
      </form>
    </Modal>
  );
}
