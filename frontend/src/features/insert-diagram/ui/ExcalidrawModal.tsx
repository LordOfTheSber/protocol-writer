import { useRef } from 'react';
import { Excalidraw } from '@excalidraw/excalidraw';
import '@excalidraw/excalidraw/index.css';
import { Button } from '@/shared/ui';
import { newSceneId, type ExcalidrawScene } from '@/shared/markdown';

// Минимальный набор методов Excalidraw API, которые нам нужны.
interface MiniExcalidrawApi {
  getSceneElements: () => readonly unknown[];
  getAppState: () => { viewBackgroundColor?: string };
  getFiles: () => Record<string, unknown>;
}

interface Props {
  initial?: ExcalidrawScene;
  onCancel: () => void;
  onSave: (scene: ExcalidrawScene) => void;
}

export default function ExcalidrawModal({ initial, onCancel, onSave }: Props) {
  const apiRef = useRef<MiniExcalidrawApi | null>(null);

  const handleSave = () => {
    const api = apiRef.current;
    if (!api) return;
    const scene: ExcalidrawScene = {
      id: initial?.id ?? newSceneId(),
      elements: api.getSceneElements(),
      appState: { viewBackgroundColor: api.getAppState().viewBackgroundColor ?? '#ffffff' },
      files: api.getFiles(),
    };
    onSave(scene);
  };

  const initialData = initial
    ? { elements: initial.elements, appState: initial.appState ?? {}, files: initial.files ?? {} }
    : undefined;

  return (
    <div className="modal-overlay" role="dialog" aria-modal="true">
      <div className="modal modal--wide">
        <div className="modal__header">
          <h3>Схема (Excalidraw)</h3>
          <div className="modal__actions">
            <Button variant="secondary" onClick={onCancel}>
              Отмена
            </Button>
            <Button onClick={handleSave}>Вставить</Button>
          </div>
        </div>
        <div className="excalidraw-host">
          <Excalidraw
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            excalidrawAPI={(api: unknown) => (apiRef.current = api as MiniExcalidrawApi)}
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            initialData={initialData as any}
          />
        </div>
      </div>
    </div>
  );
}
