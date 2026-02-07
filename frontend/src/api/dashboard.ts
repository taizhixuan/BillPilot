import api from './client';
import type { DashboardStats } from './types';

export const dashboardApi = {
  get: () => api.get<DashboardStats>('/dashboard').then(r => r.data),
};
