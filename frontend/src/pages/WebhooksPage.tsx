import { useState } from 'react';
import { useWebhooks } from '../hooks/useWebhooks';
import { DataTable, type Column } from '../components/DataTable';
import { Pagination } from '../components/Pagination';
import { Badge } from '../components/Badge';
import { TableSkeleton } from '../components/LoadingSkeleton';
import type { WebhookEvent } from '../api/types';

export function WebhooksPage() {
  const [page, setPage] = useState(0);
  const { data, isLoading } = useWebhooks(page);

  const columns: Column<WebhookEvent>[] = [
    { key: 'type', header: 'Event Type', render: (e) => <span className="font-mono text-xs font-medium">{e.eventType}</span> },
    { key: 'status', header: 'Status', render: (e) => <Badge status={e.status}>{e.status}</Badge> },
    { key: 'attempts', header: 'Attempts', render: (e) => e.attempts },
    { key: 'date', header: 'Created', render: (e) => new Date(e.createdAt).toLocaleString() },
  ];

  return (
    <div>
      <h1 className="text-2xl font-semibold text-slate-900 mb-6">Webhook Events</h1>
      {isLoading ? <TableSkeleton /> : data && (
        <>
          <DataTable columns={columns} data={data.content} keyExtractor={(e) => e.id} />
          <Pagination page={data.page} totalPages={data.totalPages} totalElements={data.totalElements} size={data.size} onPageChange={setPage} />
        </>
      )}
    </div>
  );
}
