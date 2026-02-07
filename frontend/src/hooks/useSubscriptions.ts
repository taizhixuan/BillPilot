import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { subscriptionsApi } from '../api/subscriptions';
import toast from 'react-hot-toast';

export function useSubscriptions(page = 0) {
  return useQuery({ queryKey: ['subscriptions', page], queryFn: () => subscriptionsApi.list(page) });
}

export function useCustomerSubscriptions(customerId: string, page = 0) {
  return useQuery({ queryKey: ['subscriptions', 'customer', customerId, page], queryFn: () => subscriptionsApi.listByCustomer(customerId, page), enabled: !!customerId });
}

export function useCreateSubscription() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: subscriptionsApi.create,
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['subscriptions'] }); toast.success('Subscription created'); },
    onError: () => toast.error('Failed to create subscription'),
  });
}

export function useCancelSubscription() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: subscriptionsApi.cancel,
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['subscriptions'] }); toast.success('Subscription canceled'); },
    onError: () => toast.error('Failed to cancel subscription'),
  });
}

export function useChangePlan() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: { newPlanId: string; newQuantity?: number } }) => subscriptionsApi.changePlan(id, data),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['subscriptions'] }); toast.success('Plan changed'); },
    onError: () => toast.error('Failed to change plan'),
  });
}
