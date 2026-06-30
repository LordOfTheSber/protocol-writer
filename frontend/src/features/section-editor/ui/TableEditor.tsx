import { Button } from '@/shared/ui';
import type { TableSection } from '@/entities/protocol';

interface Props {
  section: TableSection;
  onChange: (section: TableSection) => void;
}

export function TableEditor({ section, onChange }: Props) {
  const { columns, rows } = section;

  const renameColumn = (index: number, value: string) => {
    onChange({ ...section, columns: columns.map((c, i) => (i === index ? value : c)) });
  };

  const setCell = (rowIndex: number, colIndex: number, value: string) => {
    const newRows = rows.map((row, r) =>
      r === rowIndex ? row.map((cell, c) => (c === colIndex ? value : cell)) : row,
    );
    onChange({ ...section, rows: newRows });
  };

  const addColumn = () => {
    onChange({
      ...section,
      columns: [...columns, `Колонка ${columns.length + 1}`],
      rows: rows.map((row) => [...row, '']),
    });
  };

  const removeColumn = (index: number) => {
    if (columns.length <= 1) return;
    onChange({
      ...section,
      columns: columns.filter((_, i) => i !== index),
      rows: rows.map((row) => row.filter((_, i) => i !== index)),
    });
  };

  const addRow = () => onChange({ ...section, rows: [...rows, columns.map(() => '')] });
  const removeRow = (index: number) =>
    onChange({ ...section, rows: rows.filter((_, i) => i !== index) });

  return (
    <div className="table-editor">
      <table className="table">
        <thead>
          <tr>
            {columns.map((col, c) => (
              <th key={c}>
                <div className="table-editor__head">
                  <input
                    className="input"
                    value={col}
                    onChange={(e) => renameColumn(c, e.target.value)}
                  />
                  <Button variant="ghost" onClick={() => removeColumn(c)}>
                    ✕
                  </Button>
                </div>
              </th>
            ))}
            <th />
          </tr>
        </thead>
        <tbody>
          {rows.map((row, r) => (
            <tr key={r}>
              {row.map((cell, c) => (
                <td key={c}>
                  <input
                    className="input"
                    value={cell}
                    onChange={(e) => setCell(r, c, e.target.value)}
                  />
                </td>
              ))}
              <td>
                <Button variant="ghost" onClick={() => removeRow(r)}>
                  ✕
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="table-editor__actions">
        <Button variant="secondary" onClick={addColumn}>
          + Колонка
        </Button>
        <Button variant="secondary" onClick={addRow}>
          + Строка
        </Button>
      </div>
    </div>
  );
}
