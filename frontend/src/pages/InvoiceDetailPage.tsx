import { useParams, useNavigate } from 'react-router-dom';
import { useInvoice, useMarkInvoicePaid, useVoidInvoice } from '../hooks/useInvoices';
import { useProcessPayment } from '../hooks/usePayments';
import { Badge } from '../components/Badge';
import { LoadingSkeleton } from '../components/LoadingSkeleton';
import { ArrowLeftIcon } from '@heroicons/react/24/outline';

export function InvoiceDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { data: invoice, isLoading } = useInvoice(id!);
  const markPaid = useMarkInvoicePaid();
  const voidInvoice = useVoidInvoice();
  const processPayment = useProcessPayment();

  if (isLoading) return <LoadingSkeleton rows={8} />;
  if (!invoice) return <p className="text-slate-500">Invoice not found</p>;

  const canPay = invoice.status === 'OPEN' || invoice.status === 'PAST_DUE';

  return (
    <div>
      <button onClick={() => navigate('/invoices')} className="inline-flex items-center gap-1 text-sm text-slate-500 hover:text-slate-700 mb-4">
        <ArrowLeftIcon className="h-4 w-4" /> Back to Invoices
      </button>

      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-semibold text-slate-900 font-mono">{invoice.invoiceNumber}</h1>
          <p className="text-sm text-slate-500 mt-1">{invoice.customerName}</p>
        </div>
        <div className="flex items-center gap-3">
          <Badge status={invoice.status} className="text-sm px-3 py-1">{invoice.status}</Badge>
          {canPay && (
            <>
              <button onClick={() => processPayment.mutate({ invoiceId: invoice.id })} className="px-4 py-2 bg-primary-500 text-white rounded-lg text-sm font-medium hover:bg-primary-600 transition-colors">
                Process Payment
              </button>
              <button onClick={() => markPaid.mutate(invoice.id)} className="px-4 py-2 bg-emerald-500 text-white rounded-lg text-sm font-medium hover:bg-emerald-600 transition-colors">
                Mark Paid
              </button>
              <button onClick={() => voidInvoice.mutate(invoice.id)} className="px-4 py-2 text-red-600 border border-red-200 rounded-lg text-sm font-medium hover:bg-red-50 transition-colors">
                Void
              </button>
            </>
          )}
        </div>
      </div>

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        <div className="p-6 border-b border-slate-200 grid grid-cols-3 gap-6">
          <div><p className="text-xs text-slate-500">Subtotal</p><p className="text-lg font-semibold">${Number(invoice.subtotal).toLocaleString()}</p></div>
          <div><p className="text-xs text-slate-500">Tax</p><p className="text-lg font-semibold">${Number(invoice.tax).toLocaleString()}</p></div>
          <div><p className="text-xs text-slate-500">Total</p><p className="text-lg font-semibold">${Number(invoice.total).toLocaleString()}</p></div>
        </div>
        <div className="p-6">
          <h3 className="text-sm font-semibold text-slate-700 mb-3">Line Items</h3>
          <table className="w-full">
            <thead><tr className="text-xs text-slate-500 border-b"><th className="text-left py-2">Description</th><th className="text-right py-2">Qty</th><th className="text-right py-2">Unit Price</th><th className="text-right py-2">Amount</th></tr></thead>
            <tbody>
              {invoice.lineItems?.map((item) => (
                <tr key={item.id} className="border-b border-slate-50"><td className="py-2 text-sm">{item.description}</td><td className="py-2 text-sm text-right">{item.quantity}</td><td className="py-2 text-sm text-right">${Number(item.unitPrice).toLocaleString()}</td><td className="py-2 text-sm text-right font-medium">${Number(item.amount).toLocaleString()}</td></tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
