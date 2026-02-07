import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { settingsApi } from '../api/settings';
import toast from 'react-hot-toast';

export function useSettings() {
  return useQuery({ queryKey: ['settings'], queryFn: () => settingsApi.get() });
}

export function useUpdateSettings() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: settingsApi.update,
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['settings'] }); toast.success('Settings updated'); },
    onError: () => toast.error('Failed to update settings'),
  });
}
