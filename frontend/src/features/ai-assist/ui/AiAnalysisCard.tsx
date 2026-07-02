import { useMutation } from '@tanstack/react-query';
import { Button, Card } from '@/shared/ui';
import { aiApi } from '../api/aiApi';
import type { ProtocolAnalysis } from '../model/types';

/** Секция списка в результате анализа; пустые секции не рендерим. */
function AnalysisSection({ title, items }: { title: string; items: string[] }) {
  if (items.length === 0) return null;
  return (
    <div className="ai-analysis__section">
      <h4>{title}</h4>
      <ul>
        {items.map((item) => (
          <li key={item}>{item}</li>
        ))}
      </ul>
    </div>
  );
}

/**
 * Карточка AI-анализа протокола (страница чтения): по кнопке отправляет
 * документ open-source LLM и показывает структурированный разбор.
 */
export function AiAnalysisCard({ id }: { id: string }) {
  const mutation = useMutation<ProtocolAnalysis, Error>({
    mutationFn: () => aiApi.analyze(id),
  });

  return (
    <Card className="ai-analysis">
      <h3>AI-анализ</h3>
      {!mutation.data && (
        <p className="muted">
          Open-source LLM проверит полноту протокола и найдёт решения и поручения.
        </p>
      )}
      <Button type="button" variant="secondary" disabled={mutation.isPending} onClick={() => mutation.mutate()}>
        {mutation.isPending ? 'Модель думает…' : mutation.data ? 'Повторить анализ' : 'Проанализировать'}
      </Button>
      {mutation.isError && <p className="error">{mutation.error.message}</p>}
      {mutation.data && (
        <div className="ai-analysis__result">
          <p>{mutation.data.summary}</p>
          <AnalysisSection title="Сильные стороны" items={mutation.data.strengths} />
          <AnalysisSection title="Проблемы" items={mutation.data.issues} />
          <AnalysisSection title="Рекомендации" items={mutation.data.suggestions} />
          <AnalysisSection title="Решения и поручения" items={mutation.data.actionItems} />
        </div>
      )}
    </Card>
  );
}
