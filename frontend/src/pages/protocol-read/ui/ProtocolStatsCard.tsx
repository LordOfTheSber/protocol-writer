import { Card } from '@/shared/ui';
import { useProtocolStats } from '@/entities/protocol';

export function ProtocolStatsCard({ id }: { id: string }) {
  const { data, isLoading } = useProtocolStats(id);

  if (isLoading || !data) return null;

  const rows: Array<[string, number]> = [
    ['Слов', data.wordCount],
    ['Заголовков', data.headingCount],
    ['Абзацев', data.paragraphCount],
    ['Блоков кода', data.codeBlocks],
    ['Mermaid-диаграмм', data.mermaidDiagrams],
    ['Excalidraw-схем', data.excalidrawDrawings],
  ];

  return (
    <Card className="stats-card">
      <h3>Статистика</h3>
      <ul className="stats-card__list">
        {rows.map(([label, value]) => (
          <li key={label}>
            <span className="muted">{label}</span>
            <strong>{value}</strong>
          </li>
        ))}
      </ul>
    </Card>
  );
}
