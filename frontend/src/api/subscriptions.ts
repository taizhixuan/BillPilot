import api from './client';
import type { Subscription, PagedResponse } from './types';

export const subscriptionsApi = {
  list: (page = 0, size = 20) =>
    api.get<PagedResponse<Subscription>>('/subscriptions', { params: { page, size } }).then(r => r.data),
  listByCustomer: (customerId: string, page = 0, size = 20) =>
    api.get<PagedResponse<Subscription>>(`/subscriptions/customer/${customerId}`, { params: { page, size } }).then(r => r.data),
  get: (id: string) =>
    api.get<Subscription>(`/subscriptions/${id}`).then(r => r.data),
  create: (data: { customerId: string; planId: string; quantity: number; startTrial: boolean }) =>
    api.post<Subscription>('/subscriptions', data).then(r => r.data),
  cancel: (id: string) =>
    api.post<Subscription>(`/subscriptions/${id}/cancel`).then(r => r.data),
  changePlan: (id: string, data: { newPlanId: string; newQuantity?: number }) =>
    api.post<Subscription>(`/subscriptions/${id}/change-plan`, data).then(r => r.data),
};
