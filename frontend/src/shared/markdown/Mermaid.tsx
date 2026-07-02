import { useEffect, useId, useRef, useState } from 'react';
import mermaid from 'mermaid';

mermaid.initialize({ startOnLoad: false, theme: 'default', securityLevel: 'loose' });

/** Рендерит mermaid-код в SVG-диаграмму. */
export function Mermaid({ code }: { code: string }) {
  const rawId = useId();
  const id = `mermaid-${rawId.replace(/[^a-zA-Z0-9]/g, '')}`;
  const ref = useRef<HTMLDivElement>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    mermaid
      .render(id, code.trim())
      .then(({ svg }) => {
        if (!cancelled && ref.current) {
          ref.current.innerHTML = svg;
          setError(null);
        }
      })
      .catch((e: unknown) => {
        if (!cancelled) setError(e instanceof Error ? e.message : 'Ошибка рендера диаграммы');
      });
    return () => {
      cancelled = true;
    };
  }, [code, id]);

  if (error) {
    return <pre className="diagram-error">Mermaid: {error}</pre>;
  }
  return <div className="mermaid-diagram" ref={ref} />;
}
