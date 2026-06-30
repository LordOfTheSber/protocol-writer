import { apiRequest } from '@/shared/api/client';
import type {
  Protocol,
  ProtocolInput,
  ProtocolOutline,
  ProtocolStatistics,
  ProtocolSummary,
} from '../model/types';

export const protocolApi = {
  list: () => apiRequest<ProtocolSummary[]>('/protocols'),

  getById: (id: string) => apiRequest<Protocol>(`/protocols/${id}`),

  create: (input: ProtocolInput) =>
    apiRequest<Protocol>('/protocols', { method: 'POST', body: input }),

  update: (id: string, input: ProtocolInput) =>
    apiRequest<Protocol>(`/protocols/${id}`, { method: 'PUT', body: input }),

  remove: (id: string) => apiRequest<void>(`/protocols/${id}`, { method: 'DELETE' }),

  stats: (id: string) => apiRequest<ProtocolStatistics>(`/protocols/${id}/stats`),

  outline: (id: string) => apiRequest<ProtocolOutline>(`/protocols/${id}/outline`),
};
