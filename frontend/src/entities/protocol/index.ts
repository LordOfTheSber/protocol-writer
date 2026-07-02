// Публичный API слайса entities/protocol.
export type {
  OutlineHeading,
  Protocol,
  ProtocolInput,
  ProtocolOutline,
  ProtocolStatistics,
  ProtocolStatus,
  ProtocolSummary,
} from './model/types';

export { PROTOCOL_STATUS_LABELS, PROTOCOL_STATUSES } from './model/labels';

export { protocolApi } from './api/protocolApi';
export {
  protocolKeys,
  useProtocolList,
  useProtocol,
  useProtocolStats,
  useProtocolOutline,
  useCreateProtocol,
  useUpdateProtocol,
  useDeleteProtocol,
} from './api/queries';

export { ProtocolStatusBadge } from './ui/ProtocolStatusBadge';
