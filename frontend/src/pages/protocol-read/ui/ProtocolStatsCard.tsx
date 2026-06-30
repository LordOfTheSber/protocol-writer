import { Card } from '@/shared/ui';
import { DECISION_STATUS_LABELS, DECISION_STATUSES, useProtocolStats } from '@/entities/protocol';

export function ProtocolStatsCard({ id }: { id: string }) {
  const { data, isLoading } = useProtocolStats(id);

  if (isLoading || !data) return null;

  return (
    <Card className="stats-card">
      <h3>Статистика</h3>
      <ul className="stats-card__grid">
        <li>
          <strong>{data.sectionCount}</strong> секций
        </li>
        <li>
          <strong>{data.textSections}</strong> текст
        </li>
        <li>
          <strong>{data.decisionSections}</strong> списков решений
        </li>
        <li>
          <strong>{data.tableSections}</strong> таблиц
        </li>
        <li>
          <strong>{data.totalDecisions}</strong> решений
        </li>
      </ul>
      <div className="stats-card__statuses">
        {DECISION_STATUSES.map((s) => (
          <span key={s} className="muted">
            {DECISION_STATUS_LABELS[s]}: <strong>{data.decisionsByStatus[s] ?? 0}</strong>
          </span>
        ))}
      </div>
    </Card>
  );
}
