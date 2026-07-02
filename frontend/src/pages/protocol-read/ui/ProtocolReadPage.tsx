import { Link, useNavigate, useParams } from 'react-router-dom';
import { useProtocol } from '@/entities/protocol';
import { ProtocolViewer } from '@/widgets/protocol-viewer';
import { DeleteProtocolButton } from '@/features/delete-protocol';
import { AiAnalysisCard } from '@/features/ai-assist';
import { ProtocolStatsCard } from './ProtocolStatsCard';
import { ProtocolOutlineCard } from './ProtocolOutlineCard';

export function ProtocolReadPage() {
  const { id = '' } = useParams();
  const navigate = useNavigate();
  const { data, isLoading, isError, error } = useProtocol(id);

  if (isLoading) return <p className="muted">Загрузка…</p>;
  if (isError) return <p className="error">Ошибка: {(error as Error).message}</p>;
  if (!data) return null;

  return (
    <div className="page">
      <div className="page__toolbar">
        <Link className="btn btn--ghost" to="/">
          ← К списку
        </Link>
        <div className="page__toolbar-actions">
          <Link className="btn btn--secondary" to={`/protocols/${id}/edit`}>
            Редактировать
          </Link>
          <DeleteProtocolButton id={id} onDeleted={() => navigate('/')} />
        </div>
      </div>

      <div className="read-layout">
        <ProtocolViewer protocol={data} />
        <aside className="read-layout__aside">
          <ProtocolOutlineCard id={id} />
          <ProtocolStatsCard id={id} />
          <AiAnalysisCard id={id} />
        </aside>
      </div>
    </div>
  );
}
