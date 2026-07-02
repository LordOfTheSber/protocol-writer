import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { Button } from '@/shared/ui';
import { MarkdownView } from '@/shared/markdown';
import { aiApi } from '../api/aiApi';
import { IMPROVE_MODES, IMPROVE_MODE_LABELS, type ImproveMode } from '../model/types';

interface Props {
  /** Текущий markdown-текст протокола. */
  body: string;
  /** Вызывается, когда пользователь принимает предложенный ИИ вариант. */
  onApply: (body: string) => void;
}

/**
 * Панель ИИ-помощника в редакторе: прогоняет текст протокола через
 * open-source LLM (Ollama) и показывает результат в модальном предпросмотре —
 * заменить текст можно только явно.
 */
export function AiAssistPanel({ body, onApply }: Props) {
  const [result, setResult] = useState<string | null>(null);

  const mutation = useMutation({
    mutationFn: (mode: ImproveMode) => aiApi.improve(body, mode),
    onSuccess: (data) => setResult(data.text),
  });

  const close = () => {
    setResult(null);
    mutation.reset();
  };

  const empty = body.trim() === '';

  return (
    <div className="ai-panel">
      <span className="muted">ИИ-помощник:</span>
      {IMPROVE_MODES.map((mode) => (
        <Button
          key={mode}
          type="button"
          variant="secondary"
          disabled={empty || mutation.isPending}
          onClick={() => mutation.mutate(mode)}
        >
          {IMPROVE_MODE_LABELS[mode]}
        </Button>
      ))}
      {mutation.isPending && <span className="muted">Модель думает…</span>}
      {mutation.isError && <span className="error">{(mutation.error as Error).message}</span>}

      {result !== null && (
        <div className="modal-overlay">
          <div className="modal modal--wide">
            <div className="modal__header">
              <strong>Предложение ИИ</strong>
              <div className="modal__actions">
                <Button type="button" variant="secondary" onClick={close}>
                  Отмена
                </Button>
                <Button
                  type="button"
                  onClick={() => {
                    onApply(result);
                    close();
                  }}
                >
                  Заменить текст
                </Button>
              </div>
            </div>
            <div className="ai-panel__preview">
              <MarkdownView source={result} />
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
