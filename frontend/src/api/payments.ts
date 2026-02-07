import api from './client';
import type { Payment, PagedResponse } from './types';

export const paymentsApi = {
  list: (page = 0, size = 20) =>
    api.get<PagedResponse<Payment>>('/payments', { params: { page, size } }).then(r => r.data),
  listByCustomer: (customerId: string, page = 0, size = 20) =>
    api.get<PagedResponse<Payment>>(`/payments/customer/${customerId}`, { params: { page, size } }).then(r => r.data),
  process: (data: { invoiceId: string; paymentMethod?: string }) =>
    api.post<Payment>('/payments', data).then(r => r.data),
};
