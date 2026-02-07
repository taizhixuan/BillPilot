import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { plansApi } from '../api/plans';
import type { Plan } from '../api/types';
import toast from 'react-hot-toast';

export function usePlans(page = 0, size = 20) {
  return useQuery({
    queryKey: ['plans', page, size],
    queryFn: () => plansApi.list(page, size),
  });
}

export function usePlan(id: string) {
  return useQuery({
    queryKey: ['plans', id],
    queryFn: () => plansApi.get(id),
    enabled: !!id,
  });
}

export function useCreatePlan() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (data: Partial<Plan>) => plansApi.create(data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['plans'] });
      toast.success('Plan created');
    },
    onError: () => toast.error('Failed to create plan'),
  });
}

export function useUpdatePlan() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<Plan> }) => plansApi.update(id, data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['plans'] });
      toast.success('Plan updated');
    },
    onError: () => toast.error('Failed to update plan'),
  });
}

export function useDeletePlan() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => plansApi.delete(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['plans'] });
      toast.success('Plan deleted');
    },
    onError: () => toast.error('Failed to delete plan'),
  });
}
