import { useDashboard } from '../hooks/useDashboard';
import { KpiCard } from '../components/KpiCard';
import { KpiSkeleton } from '../components/LoadingSkeleton';
import {
  CurrencyDollarIcon,
  UserGroupIcon,
  ArrowTrendingDownIcon,
  ExclamationTriangleIcon,
} from '@heroicons/react/24/outline';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

export function DashboardPage() {
  const { data, isLoading } = useDashboard();

  return (
    <div>
      <h1 className="text-2xl font-semibold text-slate-900 mb-6">Dashboard</h1>

      {isLoading ? <KpiSkeleton /> : data && (
        <>
          <div className="grid grid-cols-4 gap-6 mb-8">
            <KpiCard title="Monthly Recurring Revenue" value={`$${data.mrr?.toLocaleString() ?? '0'}`} icon={CurrencyDollarIcon} />
            <KpiCard title="Active Subscriptions" value={data.activeSubscriptions} icon={UserGroupIcon} />
            <KpiCard title="Churn Rate" value={`${data.churnRate}%`} icon={ArrowTrendingDownIcon} />
            <KpiCard title="Overdue Invoices" value={data.overdueInvoices} icon={ExclamationTriangleIcon} />
          </div>

          <div className="bg-white rounded-xl border border-slate-200 p-6 shadow-sm">
            <h2 className="text-sm font-semibold text-slate-700 mb-4">Revenue Timeline</h2>
            <ResponsiveContainer width="100%" height={300}>
              <AreaChart data={data.revenueTimeline}>
                <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
                <XAxis dataKey="date" tick={{ fontSize: 12 }} stroke="#94a3b8" />
                <YAxis tick={{ fontSize: 12 }} stroke="#94a3b8" tickFormatter={(v) => `$${v}`} />
                <Tooltip formatter={(value) => [`$${value}`, 'Revenue']} />
                <Area type="monotone" dataKey="revenue" stroke="#3b82f6" fill="#3b82f6" fillOpacity={0.1} strokeWidth={2} />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </>
      )}
    </div>
  );
}
