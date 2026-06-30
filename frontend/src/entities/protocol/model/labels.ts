import type { DecisionStatus, ProtocolStatus, SectionType } from './types';

export const PROTOCOL_STATUS_LABELS: Record<ProtocolStatus, string> = {
  DRAFT: 'Черновик',
  PUBLISHED: 'Опубликован',
  ARCHIVED: 'В архиве',
};

export const DECISION_STATUS_LABELS: Record<DecisionStatus, string> = {
  OPEN: 'Открыто',
  IN_PROGRESS: 'В работе',
  DONE: 'Выполнено',
  REJECTED: 'Отклонено',
};

export const SECTION_TYPE_LABELS: Record<SectionType, string> = {
  text: 'Текст',
  decisions: 'Решения',
  table: 'Таблица',
};

export const PROTOCOL_STATUSES: ProtocolStatus[] = ['DRAFT', 'PUBLISHED', 'ARCHIVED'];
export const DECISION_STATUSES: DecisionStatus[] = ['OPEN', 'IN_PROGRESS', 'DONE', 'REJECTED'];
