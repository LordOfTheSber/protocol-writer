import { lazy, Suspense, useState } from 'react';
import MDEditor from '@uiw/react-md-editor';
import '@uiw/react-md-editor/markdown-editor.css';
import '@uiw/react-markdown-preview/markdown.css';
import { Button, Card, Field } from '@/shared/ui';
import { markdownComponents } from '@/shared/markdown';
import {
  PROTOCOL_STATUSES,
  PROTOCOL_STATUS_LABELS,
  type ProtocolInput,
  type ProtocolStatus,
} from '@/entities/protocol';
import { MERMAID_TEMPLATE, appendBlock, excalidrawBlock } from '@/features/insert-diagram';
import { AiAssistPanel } from '@/features/ai-assist';

const ExcalidrawModal = lazy(() => import('@/features/insert-diagram/ui/ExcalidrawModal'));

interface Props {
  initial?: ProtocolInput;
  submitLabel: string;
  pending?: boolean;
  errorMessage?: string;
  onSubmit: (input: ProtocolInput) => void;
}

const EMPTY: ProtocolInput = { title: '', author: '', status: 'DRAFT', body: '' };

export function ProtocolEditor({ initial, submitLabel, pending, errorMessage, onSubmit }: Props) {
  const [draft, setDraft] = useState<ProtocolInput>(initial ?? EMPTY);
  const [drawingOpen, setDrawingOpen] = useState(false);

  const patch = (p: Partial<ProtocolInput>) => setDraft((d) => ({ ...d, ...p }));
  const setBody = (body: string) => patch({ body });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({ ...draft, title: draft.title.trim() });
  };

  return (
    <form className="protocol-editor" onSubmit={handleSubmit}>
      <Card className="protocol-editor__meta">
        <Field label="Название">
          <input
            className="input"
            required
            value={draft.title}
            onChange={(e) => patch({ title: e.target.value })}
          />
        </Field>
        <div className="protocol-editor__row">
          <Field label="Автор">
            <input
              className="input"
              value={draft.author ?? ''}
              onChange={(e) => patch({ author: e.target.value })}
            />
          </Field>
          <Field label="Статус">
            <select
              className="input"
              value={draft.status}
              onChange={(e) => patch({ status: e.target.value as ProtocolStatus })}
            >
              {PROTOCOL_STATUSES.map((s) => (
                <option key={s} value={s}>
                  {PROTOCOL_STATUS_LABELS[s]}
                </option>
              ))}
            </select>
          </Field>
        </div>
      </Card>

      <div className="protocol-editor__insert">
        <span className="muted">Вставить:</span>
        <Button
          type="button"
          variant="secondary"
          onClick={() => setBody(appendBlock(draft.body, MERMAID_TEMPLATE))}
        >
          Mermaid-диаграмму
        </Button>
        <Button type="button" variant="secondary" onClick={() => setDrawingOpen(true)}>
          Схему (Excalidraw)
        </Button>
      </div>

      <AiAssistPanel body={draft.body} onApply={setBody} />

      <div className="protocol-editor__editor" data-color-mode="light">
        <MDEditor
          value={draft.body}
          onChange={(v) => setBody(v ?? '')}
          height={520}
          previewOptions={{ components: markdownComponents }}
          textareaProps={{ placeholder: 'Текст протокола в формате Markdown…' }}
        />
      </div>

      {errorMessage && <p className="error">{errorMessage}</p>}

      <div className="protocol-editor__submit">
        <Button type="submit" disabled={pending || draft.title.trim() === ''}>
          {pending ? 'Сохранение…' : submitLabel}
        </Button>
      </div>

      {drawingOpen && (
        <Suspense fallback={<div className="modal-overlay"><div className="muted">Загрузка редактора схем…</div></div>}>
          <ExcalidrawModal
            onCancel={() => setDrawingOpen(false)}
            onSave={(scene) => {
              setBody(appendBlock(draft.body, excalidrawBlock(scene)));
              setDrawingOpen(false);
            }}
          />
        </Suspense>
      )}
    </form>
  );
}
