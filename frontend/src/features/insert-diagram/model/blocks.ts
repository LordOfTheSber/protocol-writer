import { serializeExcalidrawScene, type ExcalidrawScene } from '@/shared/markdown';

export const MERMAID_TEMPLATE = ['', '```mermaid', 'flowchart TD', '  A[Старт] --> B[Готово]', '```', ''].join('\n');

export function excalidrawBlock(scene: ExcalidrawScene): string {
  return ['', '```excalidraw', serializeExcalidrawScene(scene), '```', ''].join('\n');
}

/** Дописывает блок к телу документа, гарантируя перенос строки. */
export function appendBlock(body: string, block: string): string {
  const base = body.endsWith('\n') || body.length === 0 ? body : body + '\n';
  return base + block;
}
