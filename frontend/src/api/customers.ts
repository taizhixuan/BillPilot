import api from './client';
import type { Customer, PagedResponse } from './types';

export const customersApi = {
  list: (page = 0, size = 20, search?: string) =>
    api.get<PagedResponse<Customer>>('/customers', { params: { page, size, search } }).then(r => r.data),

  get: (id: string) =>
    api.get<Customer>(`/customers/${id}`).then(r => r.data),

  create: (data: Partial<Customer>) =>
    api.post<Customer>('/customers', data).then(r => r.data),

  update: (id: string, data: Partial<Customer>) =>
    api.put<Customer>(`/customers/${id}`, data).then(r => r.data),

  delete: (id: string) =>
    api.delete(`/customers/${id}`),
};
