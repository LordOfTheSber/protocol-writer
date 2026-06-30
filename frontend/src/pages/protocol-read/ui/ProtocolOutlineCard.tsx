import { Card } from '@/shared/ui';
import { useProtocolOutline } from '@/entities/protocol';

export function ProtocolOutlineCard({ id }: { id: string }) {
  const { data, isLoading } = useProtocolOutline(id);

  if (isLoading || !data || data.headings.length === 0) return null;

  return (
    <Card className="outline-card">
      <h3>Оглавление</h3>
      <ul className="outline-card__list">
        {data.headings.map((h, i) => (
          <li key={i} style={{ paddingLeft: `${(h.level - 1) * 12}px` }}>
            {h.text}
          </li>
        ))}
      </ul>
    </Card>
  );
}
