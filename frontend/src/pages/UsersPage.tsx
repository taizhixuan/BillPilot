import { useState } from 'react';
import { useUsers, useInviteUser } from '../hooks/useUsers';
import { DataTable, type Column } from '../components/DataTable';
import { Pagination } from '../components/Pagination';
import { Badge } from '../components/Badge';
import { Modal } from '../components/Modal';
import { TableSkeleton } from '../components/LoadingSkeleton';
import { PlusIcon } from '@heroicons/react/24/outline';
import type { User, Role } from '../api/types';

export function UsersPage() {
  const [page, setPage] = useState(0);
  const [modalOpen, setModalOpen] = useState(false);
  const { data, isLoading } = useUsers(page);

  const columns: Column<User>[] = [
    { key: 'name', header: 'Name', render: (u) => <span className="font-medium text-slate-900">{u.firstName} {u.lastName}</span> },
    { key: 'email', header: 'Email', render: (u) => u.email },
    { key: 'role', header: 'Role', render: (u) => <Badge variant="info">{u.role}</Badge> },
    { key: 'status', header: 'Status', render: (u) => <Badge status={u.active ? 'ACTIVE' : 'CANCELED'}>{u.active ? 'Active' : 'Disabled'}</Badge> },
    { key: 'joined', header: 'Joined', render: (u) => new Date(u.createdAt).toLocaleDateString() },
  ];

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-slate-900">Users</h1>
        <button onClick={() => setModalOpen(true)} className="inline-flex items-center gap-2 px-4 py-2 bg-primary-500 text-white rounded-lg text-sm font-medium hover:bg-primary-600 transition-colors">
          <PlusIcon className="h-4 w-4" /> Invite User
        </button>
      </div>
      {isLoading ? <TableSkeleton /> : data && (
        <>
          <DataTable columns={columns} data={data.content} keyExtractor={(u) => u.id} />
          <Pagination page={data.page} totalPages={data.totalPages} totalElements={data.totalElements} size={data.size} onPageChange={setPage} />
        </>
      )}
      <InviteUserModal open={modalOpen} onClose={() => setModalOpen(false)} />
    </div>
  );
}

function InviteUserModal({ open, onClose }: { open: boolean; onClose: () => void }) {
  const invite = useInviteUser();
  const [form, setForm] = useState({ email: '', firstName: '', lastName: '', role: 'READ_ONLY' as Role, password: '' });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await invite.mutateAsync(form);
    setForm({ email: '', firstName: '', lastName: '', role: 'READ_ONLY', password: '' });
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose} title="Invite User">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">First Name</label>
            <input value={form.firstName} onChange={e => setForm(f => ({ ...f, firstName: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" required />
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Last Name</label>
            <input value={form.lastName} onChange={e => setForm(f => ({ ...f, lastName: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" required />
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Email</label>
          <input type="email" value={form.email} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" required />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Password</label>
          <input type="password" value={form.password} onChange={e => setForm(f => ({ ...f, password: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" required minLength={8} />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Role</label>
          <select value={form.role} onChange={e => setForm(f => ({ ...f, role: e.target.value as Role }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm">
            <option value="ADMIN">Admin</option>
            <option value="BILLING_MGR">Billing Manager</option>
            <option value="SUPPORT">Support</option>
            <option value="READ_ONLY">Read Only</option>
          </select>
        </div>
        <div className="flex justify-end gap-2 pt-2">
          <button type="button" onClick={onClose} className="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg">Cancel</button>
          <button type="submit" className="px-4 py-2 bg-primary-500 text-white rounded-lg text-sm font-medium hover:bg-primary-600">Send Invite</button>
        </div>
      </form>
    </Modal>
  );
}
