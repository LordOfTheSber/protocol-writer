import { Button, Card, Field } from '@/shared/ui';
import { SECTION_TYPE_LABELS, type Section } from '@/entities/protocol';
import { DecisionsEditor } from './DecisionsEditor';
import { TableEditor } from './TableEditor';

interface Props {
  section: Section;
  index: number;
  total: number;
  onChange: (section: Section) => void;
  onRemove: () => void;
  onMove: (direction: -1 | 1) => void;
}

export function SectionEditor({ section, index, total, onChange, onRemove, onMove }: Props) {
  const heading = (
    <Field label="Заголовок секции">
      <input
        className="input"
        value={section.heading}
        onChange={(e) => onChange({ ...section, heading: e.target.value })}
      />
    </Field>
  );

  // Switch по типу секции — фронтовый аналог pattern matching по sealed-типу.
  const body = (() => {
    switch (section.type) {
      case 'text':
        return (
          <Field label="Текст">
            <textarea
              className="input textarea"
              rows={4}
              value={section.body}
              onChange={(e) => onChange({ ...section, body: e.target.value })}
            />
          </Field>
        );
      case 'decisions':
        return <DecisionsEditor section={section} onChange={onChange} />;
      case 'table':
        return <TableEditor section={section} onChange={onChange} />;
    }
  })();

  return (
    <Card className="section-editor">
      <div className="section-editor__header">
        <span className="section-editor__type">{SECTION_TYPE_LABELS[section.type]}</span>
        <div className="section-editor__controls">
          <Button variant="ghost" disabled={index === 0} onClick={() => onMove(-1)}>
            ↑
          </Button>
          <Button variant="ghost" disabled={index === total - 1} onClick={() => onMove(1)}>
            ↓
          </Button>
          <Button variant="ghost" onClick={onRemove}>
            ✕
          </Button>
        </div>
      </div>
      {heading}
      {body}
    </Card>
  );
}
