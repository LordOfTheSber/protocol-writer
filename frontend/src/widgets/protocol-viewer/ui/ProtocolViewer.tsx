import { ProtocolStatusBadge, type Protocol } from '@/entities/protocol';
import { MarkdownView } from '@/shared/markdown';
import { formatDateTime } from '@/shared/lib/date';

export function ProtocolViewer({ protocol }: { protocol: Protocol }) {
  return (
    <article className="protocol-viewer">
      <header className="protocol-viewer__header">
        <h1>{protocol.title}</h1>
        <div className="protocol-viewer__meta">
          <ProtocolStatusBadge status={protocol.status} />
          {protocol.author && <span>Автор: {protocol.author}</span>}
          <span>Обновлён: {formatDateTime(protocol.updatedAt)}</span>
        </div>
      </header>
      <MarkdownView source={protocol.body} />
    </article>
  );
}
