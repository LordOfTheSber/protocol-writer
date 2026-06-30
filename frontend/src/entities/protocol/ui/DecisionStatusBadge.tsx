import { Badge } from '@/shared/ui';
import { DECISION_STATUS_LABELS } from '../model/labels';
import type { DecisionStatus } from '../model/types';

const TONE: Record<DecisionStatus, 'neutral' | 'info' | 'success' | 'danger'> = {
  OPEN: 'neutral',
  IN_PROGRESS: 'info',
  DONE: 'success',
  REJECTED: 'danger',
};

export function DecisionStatusBadge({ status }: { status: DecisionStatus }) {
  return <Badge tone={TONE[status]}>{DECISION_STATUS_LABELS[status]}</Badge>;
}
