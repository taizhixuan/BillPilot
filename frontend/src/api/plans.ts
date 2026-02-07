import api from './client';
import type { Plan, PagedResponse } from './types';

export const plansApi = {
  list: (page = 0, size = 20) =>
    api.get<PagedResponse<Plan>>('/plans', { params: { page, size } }).then(r => r.data),

  get: (id: string) =>
    api.get<Plan>(`/plans/${id}`).then(r => r.data),

  create: (data: Partial<Plan>) =>
    api.post<Plan>('/plans', data).then(r => r.data),

  update: (id: string, data: Partial<Plan>) =>
    api.put<Plan>(`/plans/${id}`, data).then(r => r.data),

  delete: (id: string) =>
    api.delete(`/plans/${id}`),
};
