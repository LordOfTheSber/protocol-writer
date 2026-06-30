// Excalidraw-схема, встраиваемая в markdown как блок ```excalidraw с JSON.
// Типы библиотеки сложные и меняются между версиями, поэтому держим сцену
// нестрого типизированной и приводим типы только в точке вызова exportToSvg.

export interface ExcalidrawScene {
  id: string;
  elements: readonly unknown[];
  appState?: Record<string, unknown>;
  files?: Record<string, unknown> | null;
}

export function newSceneId(): string {
  return (
    'exc-' +
    Date.now().toString(36) +
    '-' +
    Math.random().toString(36).slice(2, 8)
  );
}

export function parseExcalidrawScene(json: string): ExcalidrawScene | null {
  try {
    const data = JSON.parse(json.trim()) as Partial<ExcalidrawScene>;
    if (!data || !Array.isArray(data.elements)) return null;
    return {
      id: typeof data.id === 'string' ? data.id : newSceneId(),
      elements: data.elements,
      appState: data.appState,
      files: data.files ?? null,
    };
  } catch {
    return null;
  }
}

export function serializeExcalidrawScene(scene: ExcalidrawScene): string {
  return JSON.stringify(scene);
}
