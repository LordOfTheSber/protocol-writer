import { Link } from 'react-router-dom';
import { Card } from '@/shared/ui';
import { formatDateTime } from '@/shared/lib/date';
import { ProtocolStatusBadge, useProtocolList } from '@/entities/protocol';
import { DeleteProtocolButton } from '@/features/delete-protocol';

export function ProtocolList() {
  const { data, isLoading, isError, error } = useProtocolList();

  if (isLoading) return <p className="muted">Загрузка…</p>;
  if (isError) return <p className="error">Ошибка: {(error as Error).message}</p>;
  if (!data || data.length === 0) return <p className="muted">Протоколов пока нет.</p>;

  return (
    <ul className="protocol-list">
      {data.map((p) => (
        <li key={p.id}>
          <Card className="protocol-list__item">
            <div className="protocol-list__main">
              <Link className="protocol-list__title" to={`/protocols/${p.id}`}>
                {p.title}
              </Link>
              <div className="protocol-list__meta">
                <ProtocolStatusBadge status={p.status} />
                {p.author && <span className="muted">{p.author}</span>}
                <span className="muted">{formatDateTime(p.updatedAt)}</span>
              </div>
            </div>
            <div className="protocol-list__actions">
              <Link className="btn btn--secondary" to={`/protocols/${p.id}/edit`}>
                Редактировать
              </Link>
              <DeleteProtocolButton id={p.id} />
            </div>
          </Card>
        </li>
      ))}
    </ul>
  );
}
