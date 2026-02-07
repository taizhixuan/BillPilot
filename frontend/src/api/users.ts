import api from './client';
import type { User, PagedResponse } from './types';

export const usersApi = {
  list: (page = 0, size = 20) =>
    api.get<PagedResponse<User>>('/users', { params: { page, size } }).then(r => r.data),
  get: (id: string) =>
    api.get<User>(`/users/${id}`).then(r => r.data),
  invite: (data: { email: string; firstName: string; lastName: string; role: string; password: string }) =>
    api.post<User>('/users', data).then(r => r.data),
  update: (id: string, data: Partial<User>) =>
    api.put<User>(`/users/${id}`, data).then(r => r.data),
  delete: (id: string) =>
    api.delete(`/users/${id}`),
};
