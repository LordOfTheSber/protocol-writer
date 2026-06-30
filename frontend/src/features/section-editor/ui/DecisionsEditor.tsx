import { Button } from '@/shared/ui';
import {
  DECISION_STATUS_LABELS,
  DECISION_STATUSES,
  type Decision,
  type DecisionListSection,
} from '@/entities/protocol';

interface Props {
  section: DecisionListSection;
  onChange: (section: DecisionListSection) => void;
}

export function DecisionsEditor({ section, onChange }: Props) {
  const updateDecision = (index: number, patch: Partial<Decision>) => {
    const decisions = section.decisions.map((d, i) => (i === index ? { ...d, ...patch } : d));
    onChange({ ...section, decisions });
  };

  const addDecision = () => {
    const decision: Decision = { text: '', status: 'OPEN', assignee: '', dueDate: '' };
    onChange({ ...section, decisions: [...section.decisions, decision] });
  };

  const removeDecision = (index: number) => {
    onChange({ ...section, decisions: section.decisions.filter((_, i) => i !== index) });
  };

  return (
    <div className="decisions-editor">
      {section.decisions.map((decision, index) => (
        <div className="decisions-editor__row" key={index}>
          <input
            className="input"
            placeholder="Текст решения"
            value={decision.text}
            onChange={(e) => updateDecision(index, { text: e.target.value })}
          />
          <select
            className="input"
            value={decision.status}
            onChange={(e) => updateDecision(index, { status: e.target.value as Decision['status'] })}
          >
            {DECISION_STATUSES.map((s) => (
              <option key={s} value={s}>
                {DECISION_STATUS_LABELS[s]}
              </option>
            ))}
          </select>
          <input
            className="input"
            placeholder="Ответственный"
            value={decision.assignee ?? ''}
            onChange={(e) => updateDecision(index, { assignee: e.target.value })}
          />
          <input
            className="input"
            type="date"
            value={decision.dueDate ?? ''}
            onChange={(e) => updateDecision(index, { dueDate: e.target.value })}
          />
          <Button variant="ghost" onClick={() => removeDecision(index)}>
            ✕
          </Button>
        </div>
      ))}
      <Button variant="secondary" onClick={addDecision}>
        + Решение
      </Button>
    </div>
  );
}
