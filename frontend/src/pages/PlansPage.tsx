import { useState, useEffect, type FormEvent } from 'react';
import { usePlans, useCreatePlan, useUpdatePlan, useDeletePlan } from '../hooks/usePlans';
import { DataTable, type Column } from '../components/DataTable';
import { Pagination } from '../components/Pagination';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';
import { TableSkeleton } from '../components/LoadingSkeleton';
import { PlusIcon } from '@heroicons/react/24/outline';
import type { Plan, BillingInterval } from '../api/types';

export function PlansPage() {
  const [page, setPage] = useState(0);
  const [modalOpen, setModalOpen] = useState(false);
  const [editPlan, setEditPlan] = useState<Plan | null>(null);
  const { data, isLoading } = usePlans(page);

  const columns: Column<Plan>[] = [
    { key: 'name', header: 'Name', render: (p) => <span className="font-medium text-slate-900">{p.name}</span> },
    { key: 'price', header: 'Price', render: (p) => `$${p.price}` },
    { key: 'interval', header: 'Interval', render: (p) => <Badge status={p.billingInterval === 'MONTHLY' ? 'ACTIVE' : 'TRIALING'}>{p.billingInterval}</Badge> },
    { key: 'trial', header: 'Trial Days', render: (p) => p.trialDays },
    { key: 'status', header: 'Status', render: (p) => <Badge status={p.active ? 'ACTIVE' : 'CANCELED'}>{p.active ? 'Active' : 'Inactive'}</Badge> },
  ];

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-slate-900">Plans</h1>
        <button
          onClick={() => { setEditPlan(null); setModalOpen(true); }}
          className="inline-flex items-center gap-2 px-4 py-2 bg-primary-500 text-white rounded-lg text-sm font-medium hover:bg-primary-600 transition-colors"
        >
          <PlusIcon className="h-4 w-4" /> New Plan
        </button>
      </div>

      {isLoading ? <TableSkeleton /> : data && (
        <>
          <DataTable
            columns={columns}
            data={data.content}
            keyExtractor={(p) => p.id}
            onRowClick={(p) => { setEditPlan(p); setModalOpen(true); }}
          />
          <Pagination page={data.page} totalPages={data.totalPages} totalElements={data.totalElements} size={data.size} onPageChange={setPage} />
        </>
      )}

      <PlanFormModal
        open={modalOpen}
        plan={editPlan}
        onClose={() => setModalOpen(false)}
      />
    </div>
  );
}

function PlanFormModal({ open, plan, onClose }: { open: boolean; plan: Plan | null; onClose: () => void }) {
  const create = useCreatePlan();
  const update = useUpdatePlan();
  const del = useDeletePlan();
  const [form, setForm] = useState({ name: '', description: '', price: '', billingInterval: 'MONTHLY' as BillingInterval, trialDays: '0', features: '' });

  const resetForm = (p?: Plan | null) => {
    if (p) {
      setForm({ name: p.name, description: p.description || '', price: String(p.price), billingInterval: p.billingInterval, trialDays: String(p.trialDays), features: p.features || '' });
    } else {
      setForm({ name: '', description: '', price: '', billingInterval: 'MONTHLY', trialDays: '0', features: '' });
    }
  };

  useEffect(() => {
    if (open) resetForm(plan);
  }, [open, plan]);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    const data = { ...form, price: parseFloat(form.price), trialDays: parseInt(form.trialDays) };
    if (plan) {
      await update.mutateAsync({ id: plan.id, data });
    } else {
      await create.mutateAsync(data);
    }
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose} title={plan ? 'Edit Plan' : 'New Plan'}>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Name</label>
          <input value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" required />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Price ($)</label>
          <input type="number" step="0.01" value={form.price} onChange={e => setForm(f => ({ ...f, price: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" required />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Billing Interval</label>
            <select value={form.billingInterval} onChange={e => setForm(f => ({ ...f, billingInterval: e.target.value as BillingInterval }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500">
              <option value="MONTHLY">Monthly</option>
              <option value="YEARLY">Yearly</option>
              <option value="ONE_TIME">One Time</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Trial Days</label>
            <input type="number" value={form.trialDays} onChange={e => setForm(f => ({ ...f, trialDays: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" />
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Description</label>
          <textarea value={form.description} onChange={e => setForm(f => ({ ...f, description: e.target.value }))} rows={2} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Features</label>
          <textarea value={form.features} onChange={e => setForm(f => ({ ...f, features: e.target.value }))} rows={2} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" placeholder="Comma-separated features" />
        </div>
        <div className="flex justify-between pt-2">
          <div>
            {plan && (
              <button type="button" onClick={async () => { await del.mutateAsync(plan.id); onClose(); }} className="px-4 py-2 text-sm text-red-600 hover:bg-red-50 rounded-lg transition-colors">
                Delete
              </button>
            )}
          </div>
          <div className="flex gap-2">
            <button type="button" onClick={onClose} className="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg transition-colors">Cancel</button>
            <button type="submit" className="px-4 py-2 bg-primary-500 text-white rounded-lg text-sm font-medium hover:bg-primary-600 transition-colors">
              {plan ? 'Save Changes' : 'Create Plan'}
            </button>
          </div>
        </div>
      </form>
    </Modal>
  );
}
