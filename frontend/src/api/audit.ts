import api from './client';
import type { AuditLog, PagedResponse } from './types';

export const auditApi = {
  list: (page = 0, size = 50) =>
    api.get<PagedResponse<AuditLog>>('/audit-logs', { params: { page, size } }).then(r => r.data),
  listByEntity: (entityId: string, page = 0, size = 50) =>
    api.get<PagedResponse<AuditLog>>(`/audit-logs/entity/${entityId}`, { params: { page, size } }).then(r => r.data),
};
