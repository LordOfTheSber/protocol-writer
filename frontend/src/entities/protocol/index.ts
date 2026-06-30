// Публичный API слайса entities/protocol.
export type {
  Decision,
  DecisionStatus,
  DecisionListSection,
  Protocol,
  ProtocolInput,
  ProtocolStatistics,
  ProtocolStatus,
  ProtocolSummary,
  Section,
  SectionType,
  TableSection,
  TextSection,
} from './model/types';

export {
  PROTOCOL_STATUS_LABELS,
  DECISION_STATUS_LABELS,
  SECTION_TYPE_LABELS,
  PROTOCOL_STATUSES,
  DECISION_STATUSES,
} from './model/labels';

export { createEmptySection } from './model/factory';

export { protocolApi } from './api/protocolApi';
export type { RenderFormat } from './api/protocolApi';
export {
  protocolKeys,
  useProtocolList,
  useProtocol,
  useProtocolStats,
  useProtocolRender,
  useCreateProtocol,
  useUpdateProtocol,
  useDeleteProtocol,
} from './api/queries';

export { ProtocolStatusBadge } from './ui/ProtocolStatusBadge';
export { DecisionStatusBadge } from './ui/DecisionStatusBadge';
