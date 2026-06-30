import type { ProtocolStatus } from './types';

export const PROTOCOL_STATUS_LABELS: Record<ProtocolStatus, string> = {
  DRAFT: 'Черновик',
  PUBLISHED: 'Опубликован',
  ARCHIVED: 'В архиве',
};

export const PROTOCOL_STATUSES: ProtocolStatus[] = ['DRAFT', 'PUBLISHED', 'ARCHIVED'];
