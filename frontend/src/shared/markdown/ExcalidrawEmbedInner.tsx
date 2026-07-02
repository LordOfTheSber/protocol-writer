import { useEffect, useRef, useState } from 'react';
import { exportToSvg } from '@excalidraw/excalidraw';
import type { ExcalidrawScene } from './excalidraw';
import { parseExcalidrawScene } from './excalidraw';

/** Рендерит сохранённую Excalidraw-схему как статичный SVG (режим чтения). */
export default function ExcalidrawEmbedInner({ json }: { json: string }) {
  const ref = useRef<HTMLDivElement>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    const scene: ExcalidrawScene | null = parseExcalidrawScene(json);
    if (!scene) {
      setError('Не удалось разобрать данные схемы');
      return;
    }

    const payload = {
      elements: scene.elements,
      appState: { ...scene.appState, exportBackground: true, exportWithDarkMode: false },
      files: scene.files ?? null,
    };
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    Promise.resolve(exportToSvg(payload as any))
      .then((svg: SVGSVGElement) => {
        if (!cancelled && ref.current) {
          ref.current.innerHTML = '';
          svg.style.maxWidth = '100%';
          svg.style.height = 'auto';
          ref.current.appendChild(svg);
          setError(null);
        }
      })
      .catch((e: unknown) => {
        if (!cancelled) setError(e instanceof Error ? e.message : 'Ошибка рендера схемы');
      });

    return () => {
      cancelled = true;
    };
  }, [json]);

  if (error) {
    return <pre className="diagram-error">Excalidraw: {error}</pre>;
  }
  return <div className="excalidraw-embed" ref={ref} />;
}
