import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { usersApi } from '../api/users';
import toast from 'react-hot-toast';

export function useUsers(page = 0) {
  return useQuery({ queryKey: ['users', page], queryFn: () => usersApi.list(page) });
}

export function useInviteUser() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: usersApi.invite,
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['users'] }); toast.success('User invited'); },
    onError: () => toast.error('Failed to invite user'),
  });
}
