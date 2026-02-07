import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { invoicesApi } from '../api/invoices';
import toast from 'react-hot-toast';

export function useInvoices(page = 0) {
  return useQuery({ queryKey: ['invoices', page], queryFn: () => invoicesApi.list(page) });
}

export function useInvoice(id: string) {
  return useQuery({ queryKey: ['invoices', id], queryFn: () => invoicesApi.get(id), enabled: !!id });
}

export function useCustomerInvoices(customerId: string, page = 0) {
  return useQuery({ queryKey: ['invoices', 'customer', customerId, page], queryFn: () => invoicesApi.listByCustomer(customerId, page), enabled: !!customerId });
}

export function useMarkInvoicePaid() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: invoicesApi.markPaid,
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['invoices'] }); toast.success('Invoice marked as paid'); },
    onError: () => toast.error('Failed to mark invoice as paid'),
  });
}

export function useVoidInvoice() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: invoicesApi.void,
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['invoices'] }); toast.success('Invoice voided'); },
    onError: () => toast.error('Failed to void invoice'),
  });
}
