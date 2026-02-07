import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useInvoices } from '../hooks/useInvoices';
import { DataTable, type Column } from '../components/DataTable';
import { Pagination } from '../components/Pagination';
import { Badge } from '../components/Badge';
import { TableSkeleton } from '../components/LoadingSkeleton';
import type { Invoice } from '../api/types';

export function InvoicesPage() {
  const [page, setPage] = useState(0);
  const navigate = useNavigate();
  const { data, isLoading } = useInvoices(page);

  const columns: Column<Invoice>[] = [
    { key: 'number', header: 'Invoice #', render: (i) => <span className="font-mono text-xs font-medium text-slate-900">{i.invoiceNumber}</span> },
    { key: 'customer', header: 'Customer', render: (i) => i.customerName },
    { key: 'total', header: 'Total', render: (i) => `$${Number(i.total).toLocaleString()}` },
    { key: 'due', header: 'Due Date', render: (i) => i.dueDate },
    { key: 'status', header: 'Status', render: (i) => <Badge status={i.status}>{i.status}</Badge> },
  ];

  return (
    <div>
      <h1 className="text-2xl font-semibold text-slate-900 mb-6">Invoices</h1>
      {isLoading ? <TableSkeleton /> : data && (
        <>
          <DataTable columns={columns} data={data.content} keyExtractor={(i) => i.id} onRowClick={(i) => navigate(`/invoices/${i.id}`)} />
          <Pagination page={data.page} totalPages={data.totalPages} totalElements={data.totalElements} size={data.size} onPageChange={setPage} />
        </>
      )}
    </div>
  );
}
