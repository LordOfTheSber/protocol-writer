import { Badge } from '@/shared/ui';
import { PROTOCOL_STATUS_LABELS } from '../model/labels';
import type { ProtocolStatus } from '../model/types';

const TONE: Record<ProtocolStatus, 'neutral' | 'success' | 'warning'> = {
  DRAFT: 'neutral',
  PUBLISHED: 'success',
  ARCHIVED: 'warning',
};

export function ProtocolStatusBadge({ status }: { status: ProtocolStatus }) {
  return <Badge tone={TONE[status]}>{PROTOCOL_STATUS_LABELS[status]}</Badge>;
}
