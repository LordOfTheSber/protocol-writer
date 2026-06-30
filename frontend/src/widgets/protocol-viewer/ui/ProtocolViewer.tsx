import { ProtocolStatusBadge, type Protocol } from '@/entities/protocol';
import { formatDateTime } from '@/shared/lib/date';
import { SectionView } from './SectionView';

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

      {protocol.sections.length === 0 ? (
        <p className="muted">В протоколе пока нет секций.</p>
      ) : (
        protocol.sections.map((section, i) => <SectionView key={i} section={section} />)
      )}
    </article>
  );
}
