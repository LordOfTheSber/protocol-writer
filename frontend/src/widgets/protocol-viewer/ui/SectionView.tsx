import { DecisionStatusBadge, type Section } from '@/entities/protocol';

function DecisionDue({ dueDate }: { dueDate?: string | null }) {
  if (!dueDate) return null;
  return <span className="decision__due">срок: {dueDate}</span>;
}

/** Рендер одной секции в режиме чтения. Switch по типу — аналог pattern matching. */
export function SectionView({ section }: { section: Section }) {
  switch (section.type) {
    case 'text':
      return (
        <section className="section-view">
          {section.heading && <h3>{section.heading}</h3>}
          <p className="section-view__text">{section.body}</p>
        </section>
      );

    case 'decisions':
      return (
        <section className="section-view">
          {section.heading && <h3>{section.heading}</h3>}
          <ul className="decision-list">
            {section.decisions.map((d, i) => (
              <li className="decision" key={i}>
                <span className="decision__text">{d.text}</span>
                <DecisionStatusBadge status={d.status} />
                {d.assignee && <span className="decision__assignee">{d.assignee}</span>}
                <DecisionDue dueDate={d.dueDate} />
              </li>
            ))}
          </ul>
        </section>
      );

    case 'table':
      return (
        <section className="section-view">
          {section.heading && <h3>{section.heading}</h3>}
          <table className="table">
            <thead>
              <tr>
                {section.columns.map((c, i) => (
                  <th key={i}>{c}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {section.rows.map((row, r) => (
                <tr key={r}>
                  {row.map((cell, c) => (
                    <td key={c}>{cell}</td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      );
  }
}
