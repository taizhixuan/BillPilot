import api from './client';
import type { Invoice, PagedResponse } from './types';

export const invoicesApi = {
  list: (page = 0, size = 20) =>
    api.get<PagedResponse<Invoice>>('/invoices', { params: { page, size } }).then(r => r.data),
  listByCustomer: (customerId: string, page = 0, size = 20) =>
    api.get<PagedResponse<Invoice>>(`/invoices/customer/${customerId}`, { params: { page, size } }).then(r => r.data),
  get: (id: string) =>
    api.get<Invoice>(`/invoices/${id}`).then(r => r.data),
  markPaid: (id: string) =>
    api.post<Invoice>(`/invoices/${id}/mark-paid`).then(r => r.data),
  void: (id: string) =>
    api.post<Invoice>(`/invoices/${id}/void`).then(r => r.data),
};
