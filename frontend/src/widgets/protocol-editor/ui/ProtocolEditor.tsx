import { useState } from 'react';
import { Button, Card, Field } from '@/shared/ui';
import {
  PROTOCOL_STATUSES,
  PROTOCOL_STATUS_LABELS,
  SECTION_TYPE_LABELS,
  createEmptySection,
  type ProtocolInput,
  type ProtocolStatus,
  type Section,
  type SectionType,
} from '@/entities/protocol';
import { SectionEditor } from '@/features/section-editor';

interface Props {
  initial?: ProtocolInput;
  submitLabel: string;
  pending?: boolean;
  errorMessage?: string;
  onSubmit: (input: ProtocolInput) => void;
}

const SECTION_TYPES: SectionType[] = ['text', 'decisions', 'table'];

const EMPTY: ProtocolInput = { title: '', author: '', status: 'DRAFT', sections: [] };

export function ProtocolEditor({ initial, submitLabel, pending, errorMessage, onSubmit }: Props) {
  const [draft, setDraft] = useState<ProtocolInput>(initial ?? EMPTY);
  const [newType, setNewType] = useState<SectionType>('text');

  const patch = (p: Partial<ProtocolInput>) => setDraft((d) => ({ ...d, ...p }));

  const updateSection = (index: number, section: Section) =>
    patch({ sections: draft.sections.map((s, i) => (i === index ? section : s)) });

  const removeSection = (index: number) =>
    patch({ sections: draft.sections.filter((_, i) => i !== index) });

  const addSection = () => patch({ sections: [...draft.sections, createEmptySection(newType)] });

  const moveSection = (index: number, direction: -1 | 1) => {
    const target = index + direction;
    if (target < 0 || target >= draft.sections.length) return;
    const sections = [...draft.sections];
    [sections[index], sections[target]] = [sections[target], sections[index]];
    patch({ sections });
  };

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

      {draft.sections.map((section, index) => (
        <SectionEditor
          key={index}
          section={section}
          index={index}
          total={draft.sections.length}
          onChange={(s) => updateSection(index, s)}
          onRemove={() => removeSection(index)}
          onMove={(dir) => moveSection(index, dir)}
        />
      ))}

      <Card className="protocol-editor__add">
        <select
          className="input"
          value={newType}
          onChange={(e) => setNewType(e.target.value as SectionType)}
        >
          {SECTION_TYPES.map((t) => (
            <option key={t} value={t}>
              {SECTION_TYPE_LABELS[t]}
            </option>
          ))}
        </select>
        <Button type="button" variant="secondary" onClick={addSection}>
          + Добавить секцию
        </Button>
      </Card>

      {errorMessage && <p className="error">{errorMessage}</p>}

      <div className="protocol-editor__submit">
        <Button type="submit" disabled={pending || draft.title.trim() === ''}>
          {pending ? 'Сохранение…' : submitLabel}
        </Button>
      </div>
    </form>
  );
}
