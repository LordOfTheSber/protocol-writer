import { lazy, Suspense } from 'react';

// Excalidraw тяжёлый — грузим его только когда в документе реально есть схема.
const Inner = lazy(() => import('./ExcalidrawEmbedInner'));

export function ExcalidrawEmbed({ json }: { json: string }) {
  return (
    <Suspense fallback={<div className="muted">Загрузка схемы…</div>}>
      <Inner json={json} />
    </Suspense>
  );
}
