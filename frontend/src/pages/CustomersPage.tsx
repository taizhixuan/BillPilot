import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCustomers, useCreateCustomer } from '../hooks/useCustomers';
import { DataTable, type Column } from '../components/DataTable';
import { Pagination } from '../components/Pagination';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';
import { TableSkeleton } from '../components/LoadingSkeleton';
import { PlusIcon, MagnifyingGlassIcon } from '@heroicons/react/24/outline';
import type { Customer } from '../api/types';

export function CustomersPage() {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const navigate = useNavigate();
  const { data, isLoading } = useCustomers(page, 20, search || undefined);

  const columns: Column<Customer>[] = [
    { key: 'name', header: 'Name', render: (c) => <span className="font-medium text-slate-900">{c.name}</span> },
    { key: 'email', header: 'Email', render: (c) => c.email },
    { key: 'company', header: 'Company', render: (c) => c.company || '-' },
    { key: 'country', header: 'Country', render: (c) => c.country || '-' },
    { key: 'status', header: 'Status', render: (c) => <Badge status={c.active ? 'ACTIVE' : 'CANCELED'}>{c.active ? 'Active' : 'Inactive'}</Badge> },
  ];

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-slate-900">Customers</h1>
        <button
          onClick={() => setModalOpen(true)}
          className="inline-flex items-center gap-2 px-4 py-2 bg-primary-500 text-white rounded-lg text-sm font-medium hover:bg-primary-600 transition-colors"
        >
          <PlusIcon className="h-4 w-4" /> New Customer
        </button>
      </div>

      <div className="relative mb-4">
        <MagnifyingGlassIcon className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-slate-400" />
        <input
          type="text"
          placeholder="Search customers..."
          value={search}
          onChange={(e) => { setSearch(e.target.value); setPage(0); }}
          className="w-full pl-10 pr-4 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500 bg-white"
        />
      </div>

      {isLoading ? <TableSkeleton /> : data && (
        <>
          <DataTable
            columns={columns}
            data={data.content}
            keyExtractor={(c) => c.id}
            onRowClick={(c) => navigate(`/customers/${c.id}`)}
          />
          <Pagination page={data.page} totalPages={data.totalPages} totalElements={data.totalElements} size={data.size} onPageChange={setPage} />
        </>
      )}

      <CustomerFormModal open={modalOpen} onClose={() => setModalOpen(false)} />
    </div>
  );
}

function CustomerFormModal({ open, onClose }: { open: boolean; onClose: () => void }) {
  const create = useCreateCustomer();
  const [form, setForm] = useState({ name: '', email: '', company: '', phone: '', country: '' });

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    await create.mutateAsync(form);
    setForm({ name: '', email: '', company: '', phone: '', country: '' });
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose} title="New Customer">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Name</label>
          <input value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" required />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Email</label>
          <input type="email" value={form.email} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" required />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Company</label>
            <input value={form.company} onChange={e => setForm(f => ({ ...f, company: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Country</label>
            <input value={form.country} onChange={e => setForm(f => ({ ...f, country: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" />
          </div>
        </div>
        <div className="flex justify-end gap-2 pt-2">
          <button type="button" onClick={onClose} className="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg transition-colors">Cancel</button>
          <button type="submit" className="px-4 py-2 bg-primary-500 text-white rounded-lg text-sm font-medium hover:bg-primary-600 transition-colors">Create Customer</button>
        </div>
      </form>
    </Modal>
  );
}
