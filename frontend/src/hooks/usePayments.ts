import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { paymentsApi } from '../api/payments';
import toast from 'react-hot-toast';

export function usePayments(page = 0) {
  return useQuery({ queryKey: ['payments', page], queryFn: () => paymentsApi.list(page) });
}

export function useCustomerPayments(customerId: string, page = 0) {
  return useQuery({ queryKey: ['payments', 'customer', customerId, page], queryFn: () => paymentsApi.listByCustomer(customerId, page), enabled: !!customerId });
}

export function useProcessPayment() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: paymentsApi.process,
    onSuccess: (data) => {
      qc.invalidateQueries({ queryKey: ['payments'] });
      qc.invalidateQueries({ queryKey: ['invoices'] });
      if (data.status === 'SUCCEEDED') toast.success('Payment succeeded');
      else toast.error('Payment failed: ' + (data.failureReason || 'Unknown error'));
    },
    onError: () => toast.error('Failed to process payment'),
  });
}
