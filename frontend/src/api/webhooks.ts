import api from './client';
import type { WebhookEvent, PagedResponse } from './types';

export const webhooksApi = {
  list: (page = 0, size = 50) =>
    api.get<PagedResponse<WebhookEvent>>('/webhooks', { params: { page, size } }).then(r => r.data),
};
