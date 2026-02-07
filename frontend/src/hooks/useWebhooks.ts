import { useQuery } from '@tanstack/react-query';
import { webhooksApi } from '../api/webhooks';

export function useWebhooks(page = 0) {
  return useQuery({ queryKey: ['webhooks', page], queryFn: () => webhooksApi.list(page) });
}
