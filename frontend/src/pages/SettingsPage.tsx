import { useState, useEffect } from 'react';
import { useSettings, useUpdateSettings } from '../hooks/useSettings';
import { LoadingSkeleton } from '../components/LoadingSkeleton';

export function SettingsPage() {
  const { data: settings, isLoading } = useSettings();
  const update = useUpdateSettings();
  const [form, setForm] = useState({ invoicePrefix: '', defaultCurrency: '', trialDays: '', paymentTermsDays: '', webhookUrl: '' });

  useEffect(() => {
    if (settings) {
      setForm({
        invoicePrefix: settings.invoicePrefix || '',
        defaultCurrency: settings.defaultCurrency || '',
        trialDays: String(settings.trialDays || 14),
        paymentTermsDays: String(settings.paymentTermsDays || 30),
        webhookUrl: settings.webhookUrl || '',
      });
    }
  }, [settings]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await update.mutateAsync({
      ...form,
      trialDays: parseInt(form.trialDays),
      paymentTermsDays: parseInt(form.paymentTermsDays),
    } as any);
  };

  if (isLoading) return <LoadingSkeleton rows={6} />;

  return (
    <div>
      <h1 className="text-2xl font-semibold text-slate-900 mb-6">Settings</h1>
      <form onSubmit={handleSubmit} className="bg-white rounded-xl border border-slate-200 p-6 max-w-2xl space-y-5">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Invoice Prefix</label>
            <input value={form.invoicePrefix} onChange={e => setForm(f => ({ ...f, invoicePrefix: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" />
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Default Currency</label>
            <input value={form.defaultCurrency} onChange={e => setForm(f => ({ ...f, defaultCurrency: e.target.value }))} maxLength={3} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" />
          </div>
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Trial Days</label>
            <input type="number" value={form.trialDays} onChange={e => setForm(f => ({ ...f, trialDays: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" />
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Payment Terms (Days)</label>
            <input type="number" value={form.paymentTermsDays} onChange={e => setForm(f => ({ ...f, paymentTermsDays: e.target.value }))} className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" />
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Webhook URL</label>
          <input value={form.webhookUrl} onChange={e => setForm(f => ({ ...f, webhookUrl: e.target.value }))} placeholder="https://..." className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm" />
        </div>
        <button type="submit" className="px-6 py-2 bg-primary-500 text-white rounded-lg text-sm font-medium hover:bg-primary-600 transition-colors">
          Save Settings
        </button>
      </form>
    </div>
  );
}
