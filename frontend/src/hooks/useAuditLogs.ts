import { useQuery } from '@tanstack/react-query';
import { auditApi } from '../api/audit';

export function useAuditLogs(page = 0) {
  return useQuery({ queryKey: ['audit', page], queryFn: () => auditApi.list(page) });
}

export function useEntityAuditLogs(entityId: string, page = 0) {
  return useQuery({ queryKey: ['audit', 'entity', entityId, page], queryFn: () => auditApi.listByEntity(entityId, page), enabled: !!entityId });
}
